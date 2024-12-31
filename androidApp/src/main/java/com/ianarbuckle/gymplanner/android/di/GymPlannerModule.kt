package com.ianarbuckle.gymplanner.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.datetime.Clock

@Module
@InstallIn(SingletonComponent::class)
class GymPlannerModule {

    @Provides
    fun providesCurrentClock(): Clock {
        return Clock.System
    }
}
