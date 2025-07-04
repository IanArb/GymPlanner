package com.ianarbuckle.gymplanner.android.reporting.di

import com.ianarbuckle.gymplanner.faultreporting.DefaultFaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class FaultReportingModule {

  @Provides
  fun providesFaultReportingRepository(): FaultReportingRepository {
    return DefaultFaultReportingRepository()
  }
}
