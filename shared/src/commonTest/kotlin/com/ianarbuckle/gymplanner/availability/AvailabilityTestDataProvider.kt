package com.ianarbuckle.gymplanner.availability

import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import com.ianarbuckle.gymplanner.availability.domain.Slot
import com.ianarbuckle.gymplanner.availability.domain.Time
import com.ianarbuckle.gymplanner.availability.dto.AvailabilityDto
import com.ianarbuckle.gymplanner.availability.dto.CheckAvailabilityDto
import com.ianarbuckle.gymplanner.availability.dto.SlotDto
import com.ianarbuckle.gymplanner.availability.dto.TimeDto
import kotlinx.collections.immutable.persistentListOf

/**
 * Provides test data for AvailabilityRepository tests
 */
object AvailabilityTestDataProvider {

    // ========== Personal Trainer IDs ==========

    object PersonalTrainerIds {
        const val trainer1 = "pt-001"
        const val trainer2 = "pt-002"
        const val trainer3 = "pt-003"
        const val nonExistentTrainer = "pt-999"
    }

    // ========== Months ==========

    object Months {
        const val january = "2025-01"
        const val february = "2025-02"
        const val march = "2025-03"
        const val december = "2025-12"
        const val invalidMonth = "invalid-month"
    }

    // ========== Time DTOs ==========

    object TimeDtos {
        val morning9am = TimeDto(
            id = "time-001",
            startTime = "09:00",
            endTime = "10:00",
            status = "available"
        )

        val morning10am = TimeDto(
            id = "time-002",
            startTime = "10:00",
            endTime = "11:00",
            status = "available"
        )

        val afternoon2pm = TimeDto(
            id = "time-003",
            startTime = "14:00",
            endTime = "15:00",
            status = "booked"
        )

        val afternoon3pm = TimeDto(
            id = "time-004",
            startTime = "15:00",
            endTime = "16:00",
            status = "available"
        )

        val evening6pm = TimeDto(
            id = "time-005",
            startTime = "18:00",
            endTime = "19:00",
            status = "unavailable"
        )
    }

    // ========== Times (Domain) ==========

    object Times {
        val morning9am = Time(
            id = "time-001",
            startTime = "09:00",
            endTime = "10:00",
            status = "available"
        )

        val morning10am = Time(
            id = "time-002",
            startTime = "10:00",
            endTime = "11:00",
            status = "available"
        )

        val afternoon2pm = Time(
            id = "time-003",
            startTime = "14:00",
            endTime = "15:00",
            status = "booked"
        )

        val afternoon3pm = Time(
            id = "time-004",
            startTime = "15:00",
            endTime = "16:00",
            status = "available"
        )

        val evening6pm = Time(
            id = "time-005",
            startTime = "18:00",
            endTime = "19:00",
            status = "unavailable"
        )
    }

    // ========== Slot DTOs ==========

    object SlotDtos {
        val monday = SlotDto(
            id = "slot-001",
            date = "2025-01-06",
            times = listOf(TimeDtos.morning9am, TimeDtos.morning10am)
        )

        val tuesday = SlotDto(
            id = "slot-002",
            date = "2025-01-07",
            times = listOf(TimeDtos.afternoon2pm, TimeDtos.afternoon3pm)
        )

        val wednesday = SlotDto(
            id = "slot-003",
            date = "2025-01-08",
            times = listOf(TimeDtos.evening6pm)
        )

        val emptySlot = SlotDto(
            id = "slot-004",
            date = "2025-01-09",
            times = emptyList()
        )
    }

    // ========== Slots (Domain) ==========

    object Slots {
        val monday = Slot(
            id = "slot-001",
            date = "2025-01-06",
            times = persistentListOf(Times.morning9am, Times.morning10am)
        )

        val tuesday = Slot(
            id = "slot-002",
            date = "2025-01-07",
            times = persistentListOf(Times.afternoon2pm, Times.afternoon3pm)
        )

        val wednesday = Slot(
            id = "slot-003",
            date = "2025-01-08",
            times = persistentListOf(Times.evening6pm)
        )

        val emptySlot = Slot(
            id = "slot-004",
            date = "2025-01-09",
            times = persistentListOf()
        )
    }

    // ========== Availability DTOs ==========

    object AvailabilityDtos {
        val januaryWithSlots = AvailabilityDto(
            id = "avail-001",
            month = Months.january,
            personalTrainerId = PersonalTrainerIds.trainer1,
            slots = listOf(SlotDtos.monday, SlotDtos.tuesday, SlotDtos.wednesday)
        )

        val februaryWithSlots = AvailabilityDto(
            id = "avail-002",
            month = Months.february,
            personalTrainerId = PersonalTrainerIds.trainer2,
            slots = listOf(SlotDtos.monday)
        )

        val emptyAvailability = AvailabilityDto(
            id = "avail-003",
            month = Months.march,
            personalTrainerId = PersonalTrainerIds.trainer3,
            slots = emptyList()
        )

        val withEmptySlot = AvailabilityDto(
            id = "avail-004",
            month = Months.december,
            personalTrainerId = PersonalTrainerIds.trainer1,
            slots = listOf(SlotDtos.emptySlot)
        )
    }

    // ========== Availability (Domain) ==========

    object Availabilities {
        val januaryWithSlots = Availability(
            id = "avail-001",
            month = Months.january,
            personalTrainerId = PersonalTrainerIds.trainer1,
            slots = persistentListOf(Slots.monday, Slots.tuesday, Slots.wednesday)
        )

        val februaryWithSlots = Availability(
            id = "avail-002",
            month = Months.february,
            personalTrainerId = PersonalTrainerIds.trainer2,
            slots = persistentListOf(Slots.monday)
        )

        val emptyAvailability = Availability(
            id = "avail-003",
            month = Months.march,
            personalTrainerId = PersonalTrainerIds.trainer3,
            slots = persistentListOf()
        )

        val withEmptySlot = Availability(
            id = "avail-004",
            month = Months.december,
            personalTrainerId = PersonalTrainerIds.trainer1,
            slots = persistentListOf(Slots.emptySlot)
        )
    }

    // ========== Check Availability DTOs ==========

    object CheckAvailabilityDtos {
        val available = CheckAvailabilityDto(
            personalTrainerId = PersonalTrainerIds.trainer1,
            isAvailable = true
        )

        val unavailable = CheckAvailabilityDto(
            personalTrainerId = PersonalTrainerIds.trainer2,
            isAvailable = false
        )

        val trainer3Available = CheckAvailabilityDto(
            personalTrainerId = PersonalTrainerIds.trainer3,
            isAvailable = true
        )
    }

    // ========== Check Availability (Domain) ==========

    object CheckAvailabilities {
        val available = CheckAvailability(
            personalTrainerId = PersonalTrainerIds.trainer1,
            isAvailable = true
        )

        val unavailable = CheckAvailability(
            personalTrainerId = PersonalTrainerIds.trainer2,
            isAvailable = false
        )

        val trainer3Available = CheckAvailability(
            personalTrainerId = PersonalTrainerIds.trainer3,
            isAvailable = true
        )
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val notFound = RuntimeException("Personal trainer not found")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val serverError = RuntimeException("Internal server error")
        val invalidMonth = IllegalArgumentException("Invalid month format")
        val invalidTrainerId = IllegalArgumentException("Invalid personal trainer ID")
    }
}

