package com.ianarbuckle.gymplanner.android.workout.di

import com.ianarbuckle.gymplanner.clients.ClientsRepository
import com.ianarbuckle.gymplanner.clients.DefaultClientsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class WorkoutModule {

    @Provides
    fun providesClientRepository(): ClientsRepository {
        return DefaultClientsRepository()
    }
}
