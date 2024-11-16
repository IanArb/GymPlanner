package com.ianarbuckle.gymplanner.android.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.api.GymPlannerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GymPlannerModule {

    @Provides
    fun provideGymPlanner(dataStore: DataStore<Preferences>): GymPlanner {
        return GymPlannerFactory.create(BASE_URL, dataStore)
    }

    companion object {
        private const val BASE_URL = "https://5a7d-86-45-28-173.ngrok-free.app"
    }
}