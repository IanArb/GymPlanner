package com.ianarbuckle.gymplanner.android.gymlocations.di

import com.ianarbuckle.gymplanner.gymlocations.DefaultGymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class GymLocationsModule {

    @Provides
    fun providesGymLocationsRepository(): GymLocationsRepository {
        return DefaultGymLocationsRepository()
    }
}
