package com.jemy.robustaweather.ui.fragments.addphoto

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.jemy.robustaweather.data.entity.WeatherEntity
import com.jemy.robustaweather.data.repository.AddPhotoRepository
import com.jemy.robustaweather.data.response.WeatherResponse
import com.jemy.robustaweather.utils.Constants
import com.jemy.robustaweather.utils.Constants.Error.GENERAL
import com.jemy.robustaweather.utils.Constants.Error.NETWORK
import com.jemy.robustaweather.utils.NetworkInterceptor
import com.jemy.robustaweather.utils.Resource
import com.jemy.robustaweather.utils.ResourceState
import com.jemy.robustaweather.utils.extensions.addTo
import com.jemy.robustaweather.utils.extensions.setError
import com.jemy.robustaweather.utils.extensions.setLoading
import com.jemy.robustaweather.utils.extensions.setSuccess
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import java.io.IOException

class AddPhotoViewModel(private val repository: AddPhotoRepository) : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    var city = ""
    var temp = 0.0
    var humidity = 0.0
    var condition = ""
    var image = ""
    var wid = 0
    private val _weatherResponse = MutableLiveData<Resource<WeatherResponse>>()

    val weatherResponse: LiveData<Resource<WeatherResponse>>
        get() = _weatherResponse

    fun getWeather() {
        if (isSearchValid()) {
            repository.getWeatherFromRemote(city)
                .doOnSubscribe { _weatherResponse.setLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resource ->
                    resource?.data?.let { response ->
                        _weatherResponse.setSuccess(response)

                    } ?: _weatherResponse.setError(
                        resource.message
                    )
                }, { throwable ->
                    if (throwable.message == NetworkInterceptor.NETWORK_ISSUE) {
                        _weatherResponse.setError(NETWORK)
                    } else {
                        _weatherResponse.setError(GENERAL)
                    }
                    Log.e("MyOrdersFragment", throwable.message ?: "unknown error")
                })
                .addTo(compositeDisposable)
        }
    }

    fun addWeatherData() = liveData(Dispatchers.IO) {
        if (isValidSave()) {
            emit(Resource(ResourceState.LOADING, data = null))
            try {
                val weatherList = repository.insertWeather(
                    WeatherEntity(
                        id = wid,
                        city = city,
                        temp = temp,
                        humidity = humidity,
                        condition = condition,
                        image = image
                    )
                )
                emit(
                    Resource(
                        ResourceState.SUCCESS,
                        data = weatherList,
                        message = "Place saved successfully"
                    )
                )
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
        } else {
            emit(
                Resource(
                    ResourceState.ERROR,
                    data = null,
                    message = Constants.Error.EMPTY_FIELD
                )
            )
        }

    }

    private fun isSearchValid(): Boolean {
        var isValid = true
        if (city.isBlank()) {
            _weatherResponse.setError(Constants.Error.EMPTY_FIELD)
            isValid = false
        }
        return isValid
    }

    private fun isValidSave(): Boolean {
        var isValid = true
        if (city.isBlank() || temp == 0.0 || humidity == 0.0 || condition.isBlank() || wid == 0 || image.isBlank()) {
            isValid = false
        }
        return isValid
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}