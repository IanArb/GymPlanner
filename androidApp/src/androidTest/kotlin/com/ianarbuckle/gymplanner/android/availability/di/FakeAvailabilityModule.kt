package com.ianarbuckle.gymplanner.android.availability.di

import com.ianarbuckle.gymplanner.android.availability.fakes.FakeAvailabilityRepository
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [AvailabilityModule::class])
@Module
class FakeAvailabilityModule {

    @Singleton
    @Provides
    fun providesAvailabilityRepository(): AvailabilityRepository {
        return FakeAvailabilityRepository()
    }
}
