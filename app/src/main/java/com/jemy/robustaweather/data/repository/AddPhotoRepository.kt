package com.jemy.robustaweather.data.repository

import com.jemy.robustaweather.data.entity.WeatherEntity
import com.jemy.robustaweather.data.remote.Api
import com.jemy.robustaweather.data.response.WeatherResponse
import com.jemy.robustaweather.data.room.WeatherDao
import com.jemy.robustaweather.utils.Resource
import com.jemy.robustaweather.utils.Validator
import io.reactivex.Single
import javax.inject.Inject

class AddPhotoRepository @Inject constructor(
    private val api: Api,
    private val weatherDoa: WeatherDao,
    private val validator: Validator
) {

    fun getWeatherFromRemote(city: String): Single<Resource<WeatherResponse>> {
        return api.getWeather(city = city)
            .map { validator.validateResponse(it) }
            .map { Resource(it.state, if (it.data == null) null else it.data, it.message) }
    }

    suspend fun insertWeather(weatherEntity: WeatherEntity) =
        weatherDoa.insertWeather(weatherEntity)

}