package com.ianarbuckle.gymplanner.android.core.di

import com.ianarbuckle.gymplanner.GymPlanner
import com.ianarbuckle.gymplanner.GymPlannerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GymPlannerModule {

    @Provides
    fun provideGymPlanner(): GymPlanner = GymPlannerFactory.create(BASE_URL)

    companion object {
        private const val BASE_URL = "https://db41-86-45-28-173.ngrok-free.app"
    }
}