package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/** Fake implementation of BookingRepository for testing */
class FakeBookingRepository(private val remoteDataSource: BookingRemoteDataSource) :
    BookingRepository {

    override suspend fun saveBooking(booking: Booking): Result<BookingResponse> {
        return try {
            val result = remoteDataSource.saveBooking(booking.toBookingDto())
            Result.success(result.toBookingResponse())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun findBookingsByUserId(
        userId: String
    ): Result<ImmutableList<BookingResponse>> {
        return try {
            val bookings = remoteDataSource.findBookingsByUserId(userId)
            val bookingResponses = bookings.map { it.toBookingResponse() }.toImmutableList()
            Result.success(bookingResponses)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
