package com.ianarbuckle.gymplanner.android.availability.di

import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.DefaultAvailabilityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class AvailabilityModule {

  @Provides
  fun providesAvailabilityRepository(): AvailabilityRepository {
    return DefaultAvailabilityRepository()
  }
}
