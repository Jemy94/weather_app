package com.jemy.robustaweather.di

import com.jemy.robustaweather.data.repository.AddPhotoRepository
import com.jemy.robustaweather.data.repository.HomeRepository
import com.jemy.robustaweather.ui.fragments.addphoto.AddPhotoViewModelFactory
import com.jemy.robustaweather.ui.fragments.home.HomeViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ViewModelFactoryModule {

    @Provides
    @Singleton
    fun getHomeViewModelFactory(
        repository: HomeRepository
    ): HomeViewModelFactory = HomeViewModelFactory(repository)

    @Provides
    @Singleton
    fun getAddPhotoViewModelFactory(
        repository: AddPhotoRepository
    ): AddPhotoViewModelFactory = AddPhotoViewModelFactory(repository)
}