package com.jemy.robustaweather.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    @Json(name = "coord") val coord: Coord? =null,
    @Json(name = "weather") val weather: List<Weather?>? = emptyList(),
    @Json(name = "base") val base: String? = "",
    @Json(name = "main") val main: Main? = null,
    @Json(name = "visibility") val visibility: Int? = 0,
    @Json(name = "wind") val wind: Wind? = null,
    @Json(name = "clouds") val clouds: Clouds? = null,
    @Json(name = "dt") val dt: Int? = 0,
    @Json(name = "sys") val sys: Sys? = null,
    @Json(name = "timezone") val timezone: Int? = 0,
    @Json(name = "id") val id: Int? = 0,
    @Json(name = "name") val name: String? = "",
    @Json(name = "cod") val cod: Int? = 0
) {
    @JsonClass(generateAdapter = true)
    data class Coord(
        @Json(name = "lon") val lon: Double? = 0.0,
        @Json(name = "lat") val lat: Double? = 0.0
    )

    @JsonClass(generateAdapter = true)
    data class Weather(
        @Json(name = "id") val id: Int? = 0,
        @Json(name = "main") val main: String? = "",
        @Json(name = "description") val description: String? = "",
        @Json(name = "icon") val icon: String? = ""
    )

    @JsonClass(generateAdapter = true)
    data class Main(
        @Json(name = "temp") val temp: Double? = 0.0,
        @Json(name = "feels_like") val feelsLike: Double? = 0.0,
        @Json(name = "temp_min") val tempMin: Double? = 0.0,
        @Json(name = "temp_max") val tempMax: Double? = 0.0,
        @Json(name = "pressure") val pressure: Int? = 0,
        @Json(name = "humidity") val humidity: Int? = 0
    )

    @JsonClass(generateAdapter = true)
    data class Wind(
        @Json(name = "speed") val speed: Double? = 0.0,
        @Json(name = "deg") val deg: Int? = 0
    )

    @JsonClass(generateAdapter = true)
    data class Clouds(
        @Json(name = "all") val all: Int? = 0
    )

    @JsonClass(generateAdapter = true)
    data class Sys(
        @Json(name = "type") val type: Int? = 0,
        @Json(name = "id") val id: Int? = 0,
        @Json(name = "country") val country: String? = "",
        @Json(name = "sunrise") val sunrise: Int? = 0,
        @Json(name = "sunset") val sunset: Int? = 0
    )
}