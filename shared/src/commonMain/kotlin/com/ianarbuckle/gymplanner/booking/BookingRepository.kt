package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingMapper.toBookingDto
import com.ianarbuckle.gymplanner.booking.domain.BookingMapper.toBookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet

class BookingRepository(
    private val remoteDataSource: BookingRemoteDataSource
) {

    suspend fun saveBooking(booking: Booking): Result<BookingResponse> {
        try {
            val result = remoteDataSource.saveBooking(booking.toBookingDto())
            val bookingResponse = result.toBookingResponse()
            return Result.success(bookingResponse)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun findBookingsByUserId(userId: String): Result<ImmutableList<BookingResponse>> {
        try {
            val booking = remoteDataSource.findBookingsByUserId(userId)
            val bookingResponse = booking.map {
                it.toBookingResponse()
            }.toImmutableList()

            return Result.success(bookingResponse)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

}