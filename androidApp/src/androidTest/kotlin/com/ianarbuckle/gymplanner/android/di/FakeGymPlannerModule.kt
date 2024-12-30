package com.ianarbuckle.gymplanner.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [GymPlannerModule::class],
)
@Module
class FakeGymPlannerModule {

    @Provides
    @Singleton
    fun providesCurrentClock(): Clock {
        val fixedInstant = Instant.parse("2024-12-01T00:00:00Z")
        return FixedClock(fixedInstant)
    }
}

class FixedClock(private val fixedInstant: Instant) : Clock {
    override fun now(): Instant = fixedInstant
}
