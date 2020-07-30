package com.dew.newsapplication.utility.neworkutil

import retrofit2.Response
import java.io.IOException

open class ApiResponse<T> {

    data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

    data class ApiErrorResponse<T>(val msg: String) : ApiResponse<T>()

    class ApiEmptyResponse<T> : ApiResponse<T>()

    fun create(error: Throwable): ApiResponse<T> {
        return ApiErrorResponse(error.message ?: "Unknown error")
    }

    fun create(response: Response<T>): ApiResponse<T> {
        if (response.isSuccessful) {
            val body: T? = response.body()

            if (body == null || response.code() == 204) {
                return ApiEmptyResponse()
            } else {
                return ApiSuccessResponse(body)
            }
        } else {
            var error: String = ""
            try {
                error = response.errorBody()?.string() ?: ""

            } catch (ioExecption: IOException) {
                ioExecption.printStackTrace()
                error = response.message()
            }
            return ApiErrorResponse(error)
        }
    }


}