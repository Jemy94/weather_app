package com.jemy.robustaweather.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

abstract class NetworkInterceptor : Interceptor {

    companion object {
        const val NETWORK_ISSUE = "Network is not available"
    }

    abstract fun isInternetAvailable(): Boolean

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!isInternetAvailable()) throw IOException(NETWORK_ISSUE)
        return chain.proceed(request)
    }
}