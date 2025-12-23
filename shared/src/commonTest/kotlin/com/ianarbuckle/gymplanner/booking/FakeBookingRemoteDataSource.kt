package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.dto.BookingDto
import com.ianarbuckle.gymplanner.booking.dto.BookingResponseDto

/**
 * Fake implementation for testing BookingRepository Implements the BookingRemoteDataSource
 * interface
 */
class FakeBookingRemoteDataSource : BookingRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnSaveBooking = false
    var shouldThrowExceptionOnFindBookings = false
    var saveBookingException: Exception? = null
    var findBookingsException: Exception? = null

    // Captured calls for verification
    val saveBookingCalls = mutableListOf<BookingDto>()
    val findBookingsCalls = mutableListOf<String>()

    // Configurable responses
    var saveBookingResponse: BookingResponseDto =
        BookingTestDataProvider.BookingResponseDtos.confirmed

    var findBookingsResponse: List<BookingResponseDto> =
        BookingTestDataProvider.BookingLists.multipleBookings

    override suspend fun saveBooking(bookingDto: BookingDto): BookingResponseDto {
        saveBookingCalls.add(bookingDto)

        if (shouldThrowExceptionOnSaveBooking) {
            throw saveBookingException ?: RuntimeException("Save booking failed")
        }

        return saveBookingResponse
    }

    override suspend fun findBookingsByUserId(userId: String): List<BookingResponseDto> {
        findBookingsCalls.add(userId)

        if (shouldThrowExceptionOnFindBookings) {
            throw findBookingsException ?: RuntimeException("Find bookings failed")
        }

        return findBookingsResponse
    }

    fun reset() {
        shouldThrowExceptionOnSaveBooking = false
        shouldThrowExceptionOnFindBookings = false
        saveBookingException = null
        findBookingsException = null
        saveBookingCalls.clear()
        findBookingsCalls.clear()

        saveBookingResponse = BookingTestDataProvider.BookingResponseDtos.confirmed
        findBookingsResponse = BookingTestDataProvider.BookingLists.multipleBookings
    }
}
