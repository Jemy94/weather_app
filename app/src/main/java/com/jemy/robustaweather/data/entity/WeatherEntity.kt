package com.jemy.robustaweather.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "weather")
@Parcelize
class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = 0,
    @ColumnInfo(name = "city")
    val city: String? = "",
    @ColumnInfo(name = "temp")
    val temp: Double? = 0.0,
    @ColumnInfo(name = "humidity")
    val humidity: Double? = 0.0,
    @ColumnInfo(name = "condition")
    val condition: String? = "",
    @ColumnInfo(name = "image")
    val image: String? = ""
) : Parcelable