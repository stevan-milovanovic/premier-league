package com.smobile.premierleague.api

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import retrofit2.Response

/**
 * Unit test class for [ApiResponse]
 */
class ApiResponseTest {

    @Test
    fun testCreateException() {
        val exception = Exception("example")
        val (errorMessage) = ApiResponse.create<String>(exception)
        MatcherAssert.assertThat(errorMessage, CoreMatchers.`is`("example"))
    }

    @Test
    fun testCreateSuccess() {
        val apiResponse: ApiSuccessResponse<String> = ApiResponse
            .create<String>(Response.success("example")) as ApiSuccessResponse<String>
        MatcherAssert.assertThat(apiResponse.body, CoreMatchers.`is`("example"))
    }

    @Test
    fun testCreateError() {
        val errorResponse = Response.error<String>(
            400,
            "content".toResponseBody("application/txt".toMediaTypeOrNull())
        )
        val (errorMessage) = ApiResponse.create<String>(errorResponse) as ApiErrorResponse<String>
        MatcherAssert.assertThat(errorMessage, CoreMatchers.`is`("content"))
    }

    @Test
    fun testCreateEmptyResponseWhenStatusCode204() {
        val apiResponse: ApiEmptyResponse<String> = ApiResponse
            .create<String>(Response.success(204, "example")) as ApiEmptyResponse<String>
        MatcherAssert.assertThat(apiResponse, CoreMatchers.instanceOf(ApiEmptyResponse::class.java))
    }

    @Test
    fun testCreateEmptyResponseWhenBodyIsNull() {
        val body: String? = null
        val apiResponse: ApiEmptyResponse<String> = ApiResponse
            .create<String>(Response.success(body)) as ApiEmptyResponse<String>
        MatcherAssert.assertThat(apiResponse, CoreMatchers.instanceOf(ApiEmptyResponse::class.java))
    }

    @Test(expected = ClassCastException::class)
    fun testCreateEmptyResponseWhenBodyIsNotNull() {
        ApiResponse.create<String>(Response.success("example")) as ApiEmptyResponse<String>
    }

}