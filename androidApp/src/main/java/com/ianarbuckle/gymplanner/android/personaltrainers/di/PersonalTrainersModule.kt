package com.ianarbuckle.gymplanner.android.personaltrainers.di

import com.ianarbuckle.gymplanner.personaltrainers.DefaultPersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class PersonalTrainersModule {

    @Provides
    fun providesPersonalTrainersRepository(): PersonalTrainersRepository {
        return DefaultPersonalTrainersRepository()
    }
}
