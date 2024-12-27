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

    companion object {
        private const val BASE_URL = "https://5a7d-86-45-28-173.ngrok-free.app"
        const val MONTH_KEY = "MONTH"
    }
}