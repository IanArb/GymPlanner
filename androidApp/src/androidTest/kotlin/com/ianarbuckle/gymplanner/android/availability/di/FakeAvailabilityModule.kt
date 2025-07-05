package com.ianarbuckle.gymplanner.android.availability.di

import com.ianarbuckle.gymplanner.android.availability.fakes.FakeAvailabilityRepository
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [AvailabilityModule::class])
@Module
class FakeAvailabilityModule {

  @Provides
  fun providesAvailabilityRepository(): AvailabilityRepository {
    return FakeAvailabilityRepository()
  }
}
