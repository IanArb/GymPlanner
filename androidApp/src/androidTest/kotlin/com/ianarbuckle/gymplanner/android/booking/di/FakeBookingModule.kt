package com.ianarbuckle.gymplanner.android.booking.di

import com.ianarbuckle.gymplanner.android.booking.fakes.FakeAvailabilityRepository
import com.ianarbuckle.gymplanner.android.booking.fakes.FakeBookingRepository
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.booking.BookingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [BookingModule::class],
)
@Module
class FakeBookingModule {

    @Provides
    fun provideBookingRepository(): BookingRepository {
        return FakeBookingRepository()
    }

    @Provides
    fun providesAvailabilityRepository(): AvailabilityRepository {
        return FakeAvailabilityRepository()
    }
}
