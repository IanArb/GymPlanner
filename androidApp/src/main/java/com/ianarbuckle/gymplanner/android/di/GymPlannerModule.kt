package com.ianarbuckle.gymplanner.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Module
@InstallIn(SingletonComponent::class)
class GymPlannerModule {

  @OptIn(ExperimentalTime::class)
  @Provides
  fun providesCurrentClock(): Clock {
    return Clock.System
  }
}
