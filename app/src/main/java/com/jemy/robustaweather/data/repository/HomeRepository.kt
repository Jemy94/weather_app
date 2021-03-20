package com.jemy.robustaweather.data.repository

import com.jemy.robustaweather.data.entity.WeatherEntity
import com.jemy.robustaweather.data.room.WeatherDao
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val weatherDoa: WeatherDao
) {

    suspend fun getWeatherListFromDatabase(): List<WeatherEntity> =
        weatherDoa.getWeatherList()
}