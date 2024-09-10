package com.ianarbuckle.gymplanner.android.di

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
    fun provideGymPlanner(): GymPlanner = GymPlannerFactory.create("")
}