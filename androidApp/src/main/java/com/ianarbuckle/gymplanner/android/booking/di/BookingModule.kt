package com.ianarbuckle.gymplanner.android.booking.di

import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.DefaultAvailabilityRepository
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.DefaultBookingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class BookingModule {

    @Provides
    fun providesAvailabilityRepository(): AvailabilityRepository {
        return DefaultAvailabilityRepository()
    }

    @Provides
    fun providesBookingRepository(): BookingRepository {
        return DefaultBookingRepository()
    }
}
