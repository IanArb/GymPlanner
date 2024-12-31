package com.ianarbuckle.gymplanner.android.dashboard.di

import com.ianarbuckle.gymplanner.fitnessclass.DefaultFitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.profile.DefaultProfileRepository
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class DashboardModule {

    @Provides
    fun providesProfileRepository(): ProfileRepository {
        return DefaultProfileRepository()
    }

    @Provides
    fun providesFitnessClassRepository(): FitnessClassRepository {
        return DefaultFitnessClassRepository()
    }
}
