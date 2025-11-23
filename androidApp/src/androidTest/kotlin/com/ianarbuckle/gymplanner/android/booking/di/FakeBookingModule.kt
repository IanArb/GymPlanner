package com.ianarbuckle.gymplanner.android.booking.di

import com.ianarbuckle.gymplanner.android.booking.fakes.FakeBookingRepository
import com.ianarbuckle.gymplanner.booking.BookingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class], replaces = [BookingModule::class])
@Module
class FakeBookingModule {

    @Singleton
    @Provides
    fun provideBookingRepository(): BookingRepository {
        return FakeBookingRepository()
    }
}
