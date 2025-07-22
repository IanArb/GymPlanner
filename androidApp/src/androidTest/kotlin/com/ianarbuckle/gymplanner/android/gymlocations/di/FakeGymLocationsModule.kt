package com.ianarbuckle.gymplanner.android.gymlocations.di

import com.ianarbuckle.gymplanner.android.gymlocations.fakes.FakeGymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [GymLocationsModule::class])
@Module
class FakeGymLocationsModule {

    @Provides
    fun provideGymLocationsRepository(): GymLocationsRepository {
        return FakeGymLocationsRepository()
    }
}
