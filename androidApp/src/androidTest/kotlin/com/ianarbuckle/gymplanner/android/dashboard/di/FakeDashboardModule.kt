package com.ianarbuckle.gymplanner.android.dashboard.di

import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeFitnessClassRepository
import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeProfileRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [DashboardModule::class]
)
class FakeDashboardModule {

    @Provides
    fun providesFitnessClassesRepository(): FitnessClassRepository {
        return FakeFitnessClassRepository()
    }

    @Provides
    fun providesProfileRepository(): ProfileRepository {
        return FakeProfileRepository()
    }
}