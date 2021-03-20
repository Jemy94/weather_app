package com.jemy.robustaweather.data.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "cod") val cod: Int? = 0,
    @Json(name = "message") val message: String? = ""
)