package com.ianarbuckle.gymplanner.android.reporting.di

import com.ianarbuckle.gymplanner.android.reporting.fakes.FakeFaultRepository
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [FaultReportingModule::class])
@Module
class FakeFaultReportingModule {

    @Singleton
    @Provides
    fun provideFaultReportingRepository(): FaultReportingRepository {
        return FakeFaultRepository()
    }
}
