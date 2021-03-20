package com.jemy.robustaweather.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jemy.robustaweather.data.entity.WeatherEntity
import com.jemy.robustaweather.data.room.WeatherDao

@Database(entities = [WeatherEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        const val DATABASE_NAME = "weather_db"
    }


}