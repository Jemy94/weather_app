package com.jemy.robustaweather.data.remote

import com.jemy.robustaweather.data.response.WeatherResponse
import com.jemy.robustaweather.utils.Constants
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET(Endpoints.WEATHER)
    fun getWeather(
        @Query("q") city: String,
        @Query("units") unit: String = "metric",
        @Query("appid") apiKey: String = Constants.API_KEY
    ): Single<Response<WeatherResponse>>
}