package com.jemy.robustaweather.utils.extensions

import androidx.lifecycle.MutableLiveData
import com.jemy.robustaweather.utils.Resource
import com.jemy.robustaweather.utils.ResourceState

fun <T> MutableLiveData<Resource<T>>.setSuccess(
    data: T
) = postValue(Resource(ResourceState.SUCCESS, data))

fun <T> MutableLiveData<Resource<T>>.setLoading(
) = postValue(Resource(ResourceState.LOADING, value?.data))

fun <T> MutableLiveData<Resource<T>>.setError(
    message: String? = null
) = postValue(Resource(ResourceState.ERROR, value?.data, message))