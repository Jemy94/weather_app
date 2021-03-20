package com.jemy.robustaweather.utils

object Constants {

    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "aea9fe93701f80069337a716633be5e1"

    object Error {
        const val GENERAL = "generalError"
        const val NO_DATA = "noDataError"
        const val NETWORK = "networkError"
        const val EMPTY_FIELD = "emptyError"
    }

}