package com.smobile.premierleague.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.smobile.premierleague.model.base.Resource
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

/**
 * A NetworkBoundResource class that can provide a resource backed by
 * both the Room db and the network using the Coroutines.
 */
abstract class NetworkBoundResourceCoroutines<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()
    private val supervisorJob = SupervisorJob()

    suspend fun build(): NetworkBoundResourceCoroutines<ResultType, RequestType> {
        withContext(Dispatchers.Main) {
            result.value = Resource.loading(null)
        }
        CoroutineScope(coroutineContext).launch(supervisorJob) {
            val databaseResult = loadFromDb()
            if (shouldFetch(databaseResult)) {
                try {
                    fetchFromNetwork(databaseResult)
                } catch (e: Exception) {
                    setValue(Resource.error(e.message ?: "unknown error", loadFromDb()))
                }
            } else {
                setValue(Resource.success(databaseResult))
            }
        }
        return this
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @MainThread
    protected abstract suspend fun createCall(): RequestType

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.postValue(newValue)
        }
    }

    @MainThread
    protected abstract fun shouldFetch(databaseResult: ResultType?): Boolean

    @MainThread
    protected abstract suspend fun loadFromDb(): ResultType

    @WorkerThread
    protected abstract suspend fun saveCallResults(result: ResultType)

    @WorkerThread
    protected abstract fun processResponse(response: RequestType): ResultType

    private suspend fun fetchFromNetwork(databaseResult: ResultType) {
        setValue(Resource.loading(databaseResult))
        val networkResponse = createCall()
        saveCallResults(processResponse(networkResponse))
        setValue(Resource.success(loadFromDb()))
    }

}