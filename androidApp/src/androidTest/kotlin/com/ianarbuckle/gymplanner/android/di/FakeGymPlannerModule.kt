package com.ianarbuckle.gymplanner.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@TestInstallIn(components = [SingletonComponent::class], replaces = [GymPlannerModule::class])
@Module
class FakeGymPlannerModule {

  @OptIn(ExperimentalTime::class)
  @Provides
  @Singleton
  fun providesCurrentClock(): Clock {
    val fixedInstant = Instant.parse("2024-12-01T00:00:00Z")
    return FixedClock(fixedInstant)
  }
}

@OptIn(ExperimentalTime::class)
class FixedClock @OptIn(ExperimentalTime::class) constructor(private val fixedInstant: Instant) : Clock {
  @OptIn(ExperimentalTime::class)
  override fun now(): Instant = fixedInstant
}
