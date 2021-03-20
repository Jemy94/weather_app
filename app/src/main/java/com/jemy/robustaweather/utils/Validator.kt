package com.jemy.robustaweather.utils

import android.util.Log
import com.google.gson.Gson
import com.jemy.robustaweather.data.response.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class Validator @Inject constructor(private val gson: Gson) {

    /**
     * Validate response body to check if it's error or not
     *
     * @param response body
     */
    fun <T> validateResponse(response: Response<T>?): Resource<T> {
        response?.let {
            it.apply {
                return if (isSuccessful) {
                    Resource(ResourceState.SUCCESS, data = response.body())
                } else {
                    handleErrorBody(response.errorBody())
                }
            }
        } ?: Log.d("Null Response", "Response = null")
        return Resource(ResourceState.ERROR, message = Constants.Error.GENERAL)
    }

    private fun <T> handleErrorBody(errorBody: ResponseBody?): Resource<T> {
        val errorResponse: ErrorResponse? =
            gson.fromJson(errorBody?.string(), ErrorResponse::class.java)
        return errorResponse?.let { body ->
            try {
                if (!body.message.isNullOrBlank()) {
                    Resource<T>(ResourceState.ERROR, message = body.message)
                } else {
                    Resource<T>(ResourceState.ERROR, message = Constants.Error.GENERAL)
                }
            } catch (e: Exception) {
                e.message?.let { Log.d("Exception", it) }
                Resource<T>(ResourceState.ERROR, message = e.message)
            }
        } ?: Resource<T>(ResourceState.ERROR, message = Constants.Error.GENERAL)
    }
}