package com.smobile.premierleague.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.smobile.premierleague.api.ApiResponse
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.util.mock
import com.smobile.premierleague.util.ApiUtil
import com.smobile.premierleague.util.CountingAppExecutors
import com.smobile.premierleague.util.InstantAppExecutors
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.Mockito.*
import retrofit2.Response
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

/**
 * Unit test class for [NetworkBoundResource]
 */
@RunWith(Parameterized::class)
class NetworkBoundResourceTest(private val useRealExecutors: Boolean) {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var handleSaveCallResult: (Example) -> Unit

    private lateinit var handleShouldMatch: (Example?) -> Boolean

    private lateinit var handleCreateCall: () -> LiveData<ApiResponse<Example>>

    private val dbData = MutableLiveData<Example>()

    private lateinit var networkBoundResource: NetworkBoundResource<Example, Example>

    private val fetchedOnce = AtomicBoolean(false)
    private lateinit var countingAppExecutors: CountingAppExecutors

    init {
        if (useRealExecutors) {
            countingAppExecutors = CountingAppExecutors()
        }
    }

    @Before
    fun init() {
        val appExecutors = if (useRealExecutors)
            countingAppExecutors.appExecutors
        else
            InstantAppExecutors()
        networkBoundResource = object : NetworkBoundResource<Example, Example>(appExecutors) {
            override fun saveCallResult(item: Example) {
                handleSaveCallResult(item)
            }

            override fun shouldFetch(data: Example?): Boolean {
                // since test methods don't handle repetitive fetching, call it only once
                return handleShouldMatch(data) && fetchedOnce.compareAndSet(false, true)
            }

            override fun loadFromDb(): LiveData<Example> {
                return dbData
            }

            override fun createCall(): LiveData<ApiResponse<Example>> {
                return handleCreateCall()
            }
        }
    }

    @Test
    fun basicFromNetwork() {
        val saved = AtomicReference<Example>()
        handleShouldMatch = { it == null }
        val fetchedDbValue = Example(1)
        handleSaveCallResult = { example ->
            saved.set(example)
            dbData.setValue(fetchedDbValue)
        }
        val networkResult = Example(2)
        handleCreateCall = { ApiUtil.createCall(Response.success(networkResult)) }

        val observer: Observer<Resource<Example>> = mock()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        dbData.value = null
        drain()
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(networkResult))
        verify(observer).onChanged(Resource.success(fetchedDbValue))
    }


    @Test
    fun failureFromNetwork() {
        val saved = AtomicBoolean(false)
        handleShouldMatch = { it == null }
        handleSaveCallResult = {
            saved.set(true)
        }
        val body = "error".toResponseBody("text/html".toMediaTypeOrNull())
        handleCreateCall = { ApiUtil.createCall(Response.error(500, body)) }

        val observer: Observer<Resource<Example>> = mock()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        dbData.value = null
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))
        verify(observer).onChanged(Resource.error("error", null))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithoutNetwork() {
        val saved = AtomicBoolean(false)
        handleShouldMatch = { it == null }
        handleSaveCallResult = {
            saved.set(true)
        }

        val observer: Observer<Resource<Example>> = mock()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)
        val dbValue = Example(1)
        dbData.value = dbValue
        verify(observer).onChanged(Resource.success(dbValue))
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))
        val dbValueUpdate = Example(2)
        dbData.value = dbValueUpdate
        verify(observer).onChanged(Resource.success(dbValueUpdate))
        verifyNoMoreInteractions(observer)
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))
    }

    @Test
    fun dbSuccessWithFetchFailure() {
        val dbValue = Example(1)
        val saved = AtomicBoolean(false)
        handleShouldMatch = { value -> value === dbValue }
        handleSaveCallResult = { saved.set(true) }
        val body = "error".toResponseBody("text/html".toMediaTypeOrNull())
        val apiResponseLiveData = MutableLiveData<ApiResponse<Example>>()
        handleCreateCall = { apiResponseLiveData }

        val observer: Observer<Resource<Example>> = mock()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)

        dbData.value = dbValue
        verify(observer).onChanged(Resource.loading(dbValue))

        apiResponseLiveData.value = ApiResponse.create(Response.error(400, body))
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(false))
        verify(observer).onChanged(Resource.error("error", dbValue))

        val dbValue2 = Example(2)
        dbData.value = dbValue2
        verify(observer).onChanged(Resource.error("error", dbValue2))
        verifyNoMoreInteractions(observer)
    }

    @Test
    fun dbSuccessWithReFetchSuccess() {
        val dbValue = Example(1)
        val dbValue2 = Example(2)
        val saved = AtomicReference<Example>()
        handleShouldMatch = { example -> example === dbValue }
        handleSaveCallResult = { example ->
            saved.set(example)
            dbData.setValue(dbValue2)
        }
        val apiResponseLiveData = MutableLiveData<ApiResponse<Example>>()
        handleCreateCall = { apiResponseLiveData }

        val observer = mock<Observer<Resource<Example>>>()
        networkBoundResource.asLiveData().observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))
        reset(observer)

        dbData.value = dbValue
        val networkResult = Example(1)
        verify(observer).onChanged(Resource.loading(dbValue))
        apiResponseLiveData.value = ApiResponse.create(Response.success(networkResult))
        drain()
        MatcherAssert.assertThat(saved.get(), CoreMatchers.`is`(networkResult))
        verify(observer).onChanged(Resource.success(dbValue2))
        verifyNoMoreInteractions(observer)
    }

    private fun drain() {
        if (!useRealExecutors) {
            return
        }
        try {
            countingAppExecutors.drainTasks(1, TimeUnit.SECONDS)
        } catch (t: Throwable) {
            throw AssertionError(t)
        }
    }

    private data class Example(var value: Int)

    companion object {
        @Parameterized.Parameters
        @JvmStatic
        fun param(): List<Boolean> {
            return arrayListOf(true, false)
        }
    }

}