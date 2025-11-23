package com.ianarbuckle.gymplanner.android.dashboard.di

import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeFitnessClassRepository
import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeProfileRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DashboardModule::class])
class FakeDashboardModule {

    @Singleton
    @Provides
    fun providesFitnessClassesRepository(): FitnessClassRepository {
        return FakeFitnessClassRepository()
    }

    @Singleton
    @Provides
    fun providesProfileRepository(): ProfileRepository {
        return FakeProfileRepository()
    }
}
