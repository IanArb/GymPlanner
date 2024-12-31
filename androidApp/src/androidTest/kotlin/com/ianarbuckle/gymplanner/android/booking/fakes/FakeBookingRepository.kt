package com.ianarbuckle.gymplanner.android.booking.fakes

import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class FakeBookingRepository : BookingRepository {

    override suspend fun saveBooking(booking: Booking): Result<BookingResponse> {
        return mockSaveBookingSuccess()
    }

    override suspend fun findBookingsByUserId(userId: String): Result<ImmutableList<BookingResponse>> {
        return mockSaveBookingSuccess().map { persistentListOf(it) }
    }

    private fun mockSaveBookingSuccess(): Result<BookingResponse> {
        return Result.success(
            BookingResponse(
                userId = "6730e1cb37f4352118e0c8e1",
                clientName = "John Doe",
                bookingDate = "2022-01-01",
                bookingTime = "12:00",
                personalTrainerName = "Jane Doe",
                status = BookingStatus.CONFIRMED,
            ),
        )
    }
}
