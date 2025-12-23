package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingStatus
import com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.booking.dto.BookingResponseDto
import com.ianarbuckle.gymplanner.booking.dto.BookingStatusDto
import com.ianarbuckle.gymplanner.booking.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.datetime.LocalTime

/**
 * Provides test data for BookingRepository tests
 */
object BookingTestDataProvider {

    // ========== Personal Trainer Test Data ==========

    object PersonalTrainers {
        val johnDoe = PersonalTrainer(
            id = "pt-001",
            name = "John Doe",
            imageUrl = "https://example.com/john.jpg",
            gymLocation = GymLocation.CLONTARF
        )

        val janeSmith = PersonalTrainer(
            id = "pt-002",
            name = "Jane Smith",
            imageUrl = "https://example.com/jane.jpg",
            gymLocation = GymLocation.ASTONQUAY
        )

        val mikeBrown = PersonalTrainer(
            id = "pt-003",
            name = "Mike Brown",
            imageUrl = "https://example.com/mike.jpg",
            gymLocation = GymLocation.LEOPARDSTOWN
        )
    }

    object PersonalTrainerDtos {
        val johnDoe = PersonalTrainerDto(
            id = "pt-001",
            name = "John Doe",
            imageUrl = "https://example.com/john.jpg",
            gymLocation = GymLocation.CLONTARF
        )

        val janeSmith = PersonalTrainerDto(
            id = "pt-002",
            name = "Jane Smith",
            imageUrl = "https://example.com/jane.jpg",
            gymLocation = GymLocation.ASTONQUAY
        )

        val mikeBrown = PersonalTrainerDto(
            id = "pt-003",
            name = "Mike Brown",
            imageUrl = "https://example.com/mike.jpg",
            gymLocation = GymLocation.LEOPARDSTOWN
        )
    }

    // ========== Booking Test Data ==========

    object Bookings {
        val valid = Booking(
            timeSlotId = "ts-001",
            userId = "user-123",
            bookingDate = "2025-12-25",
            startTime = LocalTime(10, 0),
            personalTrainer = PersonalTrainers.johnDoe
        )

        val morning = Booking(
            timeSlotId = "ts-002",
            userId = "user-123",
            bookingDate = "2025-12-26",
            startTime = LocalTime(8, 0),
            personalTrainer = PersonalTrainers.janeSmith
        )

        val afternoon = Booking(
            timeSlotId = "ts-003",
            userId = "user-456",
            bookingDate = "2025-12-27",
            startTime = LocalTime(14, 30),
            personalTrainer = PersonalTrainers.mikeBrown
        )

        val withDifferentUser = Booking(
            timeSlotId = "ts-004",
            userId = "user-789",
            bookingDate = "2025-12-28",
            startTime = LocalTime(16, 0),
            personalTrainer = PersonalTrainers.johnDoe
        )
    }

    // ========== Booking Response DTOs ==========

    object BookingResponseDtos {
        val confirmed = BookingResponseDto(
            id = "booking-001",
            timeSlotId = "ts-001",
            userId = "user-123",
            bookingDate = "2025-12-25",
            startTime = "10:00",
            personalTrainer = PersonalTrainerDtos.johnDoe,
            status = BookingStatusDto.CONFIRMED
        )

        val pending = BookingResponseDto(
            id = "booking-002",
            timeSlotId = "ts-002",
            userId = "user-123",
            bookingDate = "2025-12-26",
            startTime = "08:00",
            personalTrainer = PersonalTrainerDtos.janeSmith,
            status = BookingStatusDto.PENDING
        )

        val cancelled = BookingResponseDto(
            id = "booking-003",
            timeSlotId = "ts-003",
            userId = "user-456",
            bookingDate = "2025-12-27",
            startTime = "14:30",
            personalTrainer = PersonalTrainerDtos.mikeBrown,
            status = BookingStatusDto.CANCELLED
        )

        val completed = BookingResponseDto(
            id = "booking-004",
            timeSlotId = "ts-004",
            userId = "user-123",
            bookingDate = "2025-12-20",
            startTime = "10:00",
            personalTrainer = PersonalTrainerDtos.johnDoe,
            status = BookingStatusDto.COMPLETED
        )
    }

    // ========== Booking Domain Responses ==========

    object BookingResponses {
        val confirmed = BookingResponse(
            timeSlotId = "ts-001",
            userId = "user-123",
            bookingDate = "2025-12-25",
            startTime = "10:00",
            personalTrainer = PersonalTrainers.johnDoe,
            status = BookingStatus.CONFIRMED
        )

        val pending = BookingResponse(
            timeSlotId = "ts-002",
            userId = "user-123",
            bookingDate = "2025-12-26",
            startTime = "08:00",
            personalTrainer = PersonalTrainers.janeSmith,
            status = BookingStatus.PENDING
        )

        val cancelled = BookingResponse(
            timeSlotId = "ts-003",
            userId = "user-456",
            bookingDate = "2025-12-27",
            startTime = "14:30",
            personalTrainer = PersonalTrainers.mikeBrown,
            status = BookingStatus.CANCELLED
        )

        val completed = BookingResponse(
            timeSlotId = "ts-004",
            userId = "user-123",
            bookingDate = "2025-12-20",
            startTime = "10:00",
            personalTrainer = PersonalTrainers.johnDoe,
            status = BookingStatus.COMPLETED
        )
    }

    // ========== Lists of Bookings ==========

    object BookingLists {
        val multipleBookings = listOf(
            BookingResponseDtos.confirmed,
            BookingResponseDtos.pending,
            BookingResponseDtos.completed
        )

        val singleBooking = listOf(BookingResponseDtos.confirmed)

        val emptyList = emptyList<BookingResponseDto>()
    }

    object BookingResponseLists {
        val multipleBookings = persistentListOf(
            BookingResponses.confirmed,
            BookingResponses.pending,
            BookingResponses.completed
        )

        val singleBooking = persistentListOf(BookingResponses.confirmed)

        val emptyList = persistentListOf<BookingResponse>()
    }

    // ========== User IDs ==========

    object UserIds {
        const val user123 = "user-123"
        const val user456 = "user-456"
        const val user789 = "user-789"
        const val nonExistentUser = "user-999"
    }

    // ========== Exceptions ==========

    object Exceptions {
        val bookingConflict = RuntimeException("Booking conflict - time slot already taken")
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("Booking not found")
        val validationError = IllegalArgumentException("Invalid booking data")
    }
}

