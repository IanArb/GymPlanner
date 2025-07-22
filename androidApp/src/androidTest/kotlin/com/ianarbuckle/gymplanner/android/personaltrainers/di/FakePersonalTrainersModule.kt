package com.ianarbuckle.gymplanner.android.personaltrainers.di

import com.ianarbuckle.gymplanner.android.personaltrainers.fakes.FakePersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [PersonalTrainersModule::class])
@Module
class FakePersonalTrainersModule {

    @Provides
    fun providePersonalTrainersRepository(): PersonalTrainersRepository {
        return FakePersonalTrainersRepository()
    }
}
