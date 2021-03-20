package com.jemy.robustaweather.ui.fragments.addphoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jemy.robustaweather.data.repository.AddPhotoRepository
import javax.inject.Inject

class AddPhotoViewModelFactory @Inject constructor(
    private val repository: AddPhotoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddPhotoViewModel::class.java)) {
            return AddPhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class name need ${AddPhotoViewModel::class.java.simpleName} instance")
    }
}