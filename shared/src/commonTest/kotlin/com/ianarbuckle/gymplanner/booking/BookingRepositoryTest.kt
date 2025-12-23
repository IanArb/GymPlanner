package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.BookingLists
import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.BookingResponseDtos
import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.BookingResponseLists
import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.BookingResponses
import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.Bookings
import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.booking.BookingTestDataProvider.UserIds
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BookingRepositoryTest {

    private lateinit var repository: BookingRepository
    private lateinit var fakeRemoteDataSource: FakeBookingRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeBookingRemoteDataSource()
        repository = FakeBookingRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Save Booking Tests ==========

    @Test
    fun `saveBooking with valid data returns success with booking response`() = runTest {
        // Given
        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.confirmed

        // When
        val result = repository.saveBooking(Bookings.valid)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(BookingResponses.confirmed, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.saveBookingCalls.size)
    }

    @Test
    fun `saveBooking calls remote data source with correct booking data`() = runTest {
        // When
        repository.saveBooking(Bookings.valid)

        // Then
        assertEquals(1, fakeRemoteDataSource.saveBookingCalls.size)
        val capturedBooking = fakeRemoteDataSource.saveBookingCalls[0]
        assertEquals("ts-001", capturedBooking.timeSlotId)
        assertEquals("user-123", capturedBooking.userId)
        assertEquals("2025-12-25", capturedBooking.bookingDate)
    }

    @Test
    fun `saveBooking with booking conflict returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveBooking = true
        fakeRemoteDataSource.saveBookingException = Exceptions.bookingConflict

        // When
        val result = repository.saveBooking(Bookings.valid)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.bookingConflict, result.exceptionOrNull())
    }

    @Test
    fun `saveBooking with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveBooking = true
        fakeRemoteDataSource.saveBookingException = Exceptions.networkError

        // When
        val result = repository.saveBooking(Bookings.morning)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `saveBooking with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveBooking = true
        fakeRemoteDataSource.saveBookingException = Exceptions.serverError

        // When
        val result = repository.saveBooking(Bookings.afternoon)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `saveBooking with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveBooking = true
        fakeRemoteDataSource.saveBookingException = Exceptions.unauthorized

        // When
        val result = repository.saveBooking(Bookings.valid)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `saveBooking maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.pending

        // When
        val result = repository.saveBooking(Bookings.morning)

        // Then
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(BookingResponses.pending, response)
    }

    @Test
    fun `multiple saveBooking calls work independently`() = runTest {
        // When
        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.confirmed
        val result1 = repository.saveBooking(Bookings.valid)

        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.pending
        val result2 = repository.saveBooking(Bookings.morning)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeRemoteDataSource.saveBookingCalls.size)
    }

    @Test
    fun `saveBooking with different booking statuses is handled`() = runTest {
        // Given - Test with cancelled status
        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.cancelled

        // When
        val result = repository.saveBooking(Bookings.afternoon)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(BookingResponses.cancelled, result.getOrNull())
    }

    // ========== Find Bookings Tests ==========

    @Test
    fun `findBookingsByUserId with valid user returns list of bookings`() = runTest {
        // Given
        fakeRemoteDataSource.findBookingsResponse = BookingLists.multipleBookings

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(BookingResponseLists.multipleBookings, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.findBookingsCalls.size)
        assertEquals(UserIds.user123, fakeRemoteDataSource.findBookingsCalls[0])
    }

    @Test
    fun `findBookingsByUserId with single booking returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.findBookingsResponse = BookingLists.singleBooking

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(BookingResponseLists.singleBooking, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `findBookingsByUserId with no bookings returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.findBookingsResponse = BookingLists.emptyList

        // When
        val result = repository.findBookingsByUserId(UserIds.nonExistentUser)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(BookingResponseLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `findBookingsByUserId with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindBookings = true
        fakeRemoteDataSource.findBookingsException = Exceptions.networkError

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `findBookingsByUserId with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindBookings = true
        fakeRemoteDataSource.findBookingsException = Exceptions.unauthorized

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `findBookingsByUserId maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.findBookingsResponse = BookingLists.multipleBookings

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        val bookings = result.getOrNull()
        assertNotNull(bookings)
        assertEquals(3, bookings.size)
        assertEquals(BookingResponseLists.multipleBookings, bookings)
    }

    @Test
    fun `findBookingsByUserId with different users works independently`() = runTest {
        // When
        fakeRemoteDataSource.findBookingsResponse = BookingLists.multipleBookings
        val result1 = repository.findBookingsByUserId(UserIds.user123)

        fakeRemoteDataSource.findBookingsResponse = BookingLists.singleBooking
        val result2 = repository.findBookingsByUserId(UserIds.user456)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(3, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
        assertEquals(2, fakeRemoteDataSource.findBookingsCalls.size)
    }

    @Test
    fun `findBookingsByUserId returns immutable list`() = runTest {
        // Given
        fakeRemoteDataSource.findBookingsResponse = BookingLists.multipleBookings

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        val bookings = result.getOrNull()
        assertNotNull(bookings)
        // Verify it's an ImmutableList by checking the type
        assertTrue(bookings::class.simpleName?.contains("Immutable") == true
            || bookings::class.simpleName?.contains("Persistent") == true)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `saveBooking handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveBooking = true
        fakeRemoteDataSource.saveBookingException = Exceptions.validationError

        // When
        val result = repository.saveBooking(Bookings.valid)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `findBookingsByUserId handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindBookings = true
        fakeRemoteDataSource.findBookingsException = Exceptions.notFound

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `successful saveBooking does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.saveBooking(Bookings.valid)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `successful findBookingsByUserId does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.findBookingsByUserId(UserIds.user123)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `saveBooking with different personal trainers is handled`() = runTest {
        // When
        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.confirmed
        val result1 = repository.saveBooking(Bookings.valid)

        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.pending
        val result2 = repository.saveBooking(Bookings.morning)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertNotNull(result1.getOrNull()?.personalTrainer)
        assertNotNull(result2.getOrNull()?.personalTrainer)
    }

    @Test
    fun `findBookingsByUserId with empty string userId calls remote data source`() = runTest {
        // When
        val result = repository.findBookingsByUserId("")

        // Then
        assertEquals(1, fakeRemoteDataSource.findBookingsCalls.size)
        assertEquals("", fakeRemoteDataSource.findBookingsCalls[0])
    }

    @Test
    fun `saveBooking response contains all required fields`() = runTest {
        // Given
        fakeRemoteDataSource.saveBookingResponse = BookingResponseDtos.confirmed

        // When
        val result = repository.saveBooking(Bookings.valid)

        // Then
        val booking = result.getOrNull()
        assertNotNull(booking)
        assertNotNull(booking.timeSlotId)
        assertNotNull(booking.userId)
        assertNotNull(booking.bookingDate)
        assertNotNull(booking.startTime)
        assertNotNull(booking.personalTrainer)
        assertNotNull(booking.status)
    }

    @Test
    fun `findBookingsByUserId with completed bookings is handled`() = runTest {
        // Given
        fakeRemoteDataSource.findBookingsResponse = listOf(BookingResponseDtos.completed)

        // When
        val result = repository.findBookingsByUserId(UserIds.user123)

        // Then
        assertTrue(result.isSuccess)
        val bookings = result.getOrNull()
        assertNotNull(bookings)
        assertEquals(1, bookings.size)
        assertEquals(BookingResponses.completed.status, bookings[0].status)
    }
}

