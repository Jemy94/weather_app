package com.jemy.robustaweather.ui.fragments.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jemy.robustaweather.data.repository.HomeRepository
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(
    private val repository: HomeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name need ${HomeViewModel::class.java.simpleName} instance")
    }
}