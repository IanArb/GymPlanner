package com.ianarbuckle.gymplanner.android.personaltrainers.di

import com.ianarbuckle.gymplanner.android.personaltrainers.fakes.FakePersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [PersonalTrainersModule::class])
@Module
class FakePersonalTrainersModule {

    @Provides
    @Singleton
    fun providePersonalTrainersRepository(): PersonalTrainersRepository {
        return FakePersonalTrainersRepository()
    }
}
