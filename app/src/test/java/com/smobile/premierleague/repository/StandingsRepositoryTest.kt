package com.smobile.premierleague.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.smobile.premierleague.api.ApiResponse
import com.smobile.premierleague.api.LeagueService
import com.smobile.premierleague.api.StandingsNetworkResponse
import com.smobile.premierleague.db.LeagueDb
import com.smobile.premierleague.db.StandingDao
import com.smobile.premierleague.model.Standing
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.util.mock
import com.smobile.premierleague.util.AbsentLiveData
import com.smobile.premierleague.util.ApiUtil.successCall
import com.smobile.premierleague.util.InstantAppExecutors
import com.smobile.premierleague.util.TestUtil.createStandingsNetworkResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

/**
 * Unit test class for [StandingsRepository]
 */
class StandingsRepositoryTest {

    private lateinit var repository: StandingsRepository
    private val dao: StandingDao = mock(StandingDao::class.java)
    private val service: LeagueService = mock(LeagueService::class.java)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db: LeagueDb = mock(LeagueDb::class.java)
        `when`(db.standingDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = StandingsRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadStandingsFromNetwork() {
        val dbData = MutableLiveData<List<Standing>>()
        `when`(dao.getAll()).thenReturn(dbData)
        val standingsNetworkResponse = createStandingsNetworkResponse()
        val call = successCall(standingsNetworkResponse)
        `when`(service.getStandings(100)).thenReturn(call)

        val data = repository.loadStandings(100)
        verify(dao).getAll()
        verifyNoMoreInteractions(service)

        val observer = mock<Observer<Resource<List<Standing>>>>()
        data.observeForever(observer)
        verifyNoMoreInteractions(service)
        verify(observer).onChanged(Resource.loading(null))
        val updatedDbData = MutableLiveData<List<Standing>>()
        `when`(dao.getAll()).thenReturn(updatedDbData)

        dbData.postValue(null)
        verify(service).getStandings(100)
        verify(dao).insert(standingsNetworkResponse.api.standings[0])

        updatedDbData.postValue(standingsNetworkResponse.api.standings[0])
        verify(observer).onChanged(Resource.success(standingsNetworkResponse.api.standings[0]))
    }

    @Test
    fun loadStandingsFromNetworkError() {
        `when`(dao.getAll()).thenReturn(AbsentLiveData.create())
        val apiResponse = MutableLiveData<ApiResponse<StandingsNetworkResponse>>()
        `when`(service.getStandings(100)).thenReturn(apiResponse)

        val observer = mock<Observer<Resource<List<Standing>>>>()
        repository.loadStandings(100).observeForever(observer)
        verify(observer).onChanged(Resource.loading(null))

        apiResponse.postValue(ApiResponse.create(Exception("example")))
        verify(observer).onChanged(Resource.error("example", null))
    }

}