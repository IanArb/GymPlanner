package com.ianarbuckle.gymplanner.android.gymlocations.di

import com.ianarbuckle.gymplanner.android.gymlocations.fakes.FakeGymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [GymLocationsModule::class])
@Module
class FakeGymLocationsModule {

    @Singleton
    @Provides
    fun provideGymLocationsRepository(): GymLocationsRepository {
        return FakeGymLocationsRepository()
    }
}
