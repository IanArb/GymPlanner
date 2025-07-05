package com.ianarbuckle.gymplanner.android.reporting.di

import com.ianarbuckle.gymplanner.android.reporting.fakes.FakeFaultRepository
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(components = [ViewModelComponent::class], replaces = [FaultReportingModule::class])
@Module
class FakeFaultReportingModule {

  @Provides
  fun provideFaultReportingRepository(): FaultReportingRepository {
    return FakeFaultRepository()
  }
}
