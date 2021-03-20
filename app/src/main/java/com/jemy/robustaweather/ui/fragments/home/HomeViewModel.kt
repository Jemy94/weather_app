package com.jemy.robustaweather.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jemy.robustaweather.data.repository.HomeRepository
import com.jemy.robustaweather.utils.Resource
import com.jemy.robustaweather.utils.ResourceState
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    fun getWeatherList() = liveData(Dispatchers.IO) {
        emit(Resource(ResourceState.LOADING, data = null))
        try {
            val weatherList = repository.getWeatherListFromDatabase()
            emit(Resource(ResourceState.SUCCESS, data = weatherList))
        } catch (exception: Exception) {
            emit(
                Resource(
                    ResourceState.ERROR,
                    data = null,
                    message = exception.message ?: "Unknown error"
                )
            )
        } catch (exception: IOException) {
            Resource(
                ResourceState.ERROR,
                data = null,
                message = exception.message ?: "Unknown error"
            )
        }
    }
}