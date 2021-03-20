package com.jemy.robustaweather.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jemy.robustaweather.data.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(vararg weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather")
    suspend fun getWeatherList(): List<WeatherEntity>
}