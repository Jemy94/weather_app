package com.jemy.robustaweather.utils


class Resource<out T> constructor(
    val state: ResourceState,
    val data: T? = null,
    val message: String? = null
)