package com.smobile.premierleague.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.smobile.premierleague.model.base.Resource
import com.smobile.premierleague.model.base.Status
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

fun <T, K> fetchData(
    databaseQuery: () -> LiveData<T>,
    networkCall: suspend () -> Response<K>,
    saveCallResult: suspend (K) -> Unit
): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading<T>())
        val source: LiveData<Resource<T>> = databaseQuery.invoke().map { Resource.success(it) }
        emitSource(source)

        val responseStatus = getResult(networkCall)
        if (responseStatus.status == Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)
        } else if (responseStatus.status == Status.ERROR) {
            emit(Resource.error<T>(responseStatus.message ?: "unknown error", null))
            emitSource(source)
        }
    }

private suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
    try {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.success(body)
        }
        return error(" ${response.code()} ${response.message()}")
    } catch (e: Exception) {
        return error(e.message ?: e.toString())
    }
}

private fun <T> error(message: String): Resource<T> {
    return Resource.error("Network call has failed for a following reason: $message")
}