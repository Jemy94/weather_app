package com.jemy.robustaweather.utils

sealed class ResourceState {

    object LOADING : ResourceState()
    object SUCCESS : ResourceState()
    object ERROR : ResourceState()
}