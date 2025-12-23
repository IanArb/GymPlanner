package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.fitnessclass.domain.Duration
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.fitnessclass.dto.DurationDto
import com.ianarbuckle.gymplanner.fitnessclass.dto.FitnessClassDto

/**
 * Provides test data for FitnessClassRepository tests
 */
object FitnessClassTestDataProvider {

    // ========== Days of Week ==========

    object DaysOfWeek {
        const val monday = "Monday"
        const val tuesday = "Tuesday"
        const val wednesday = "Wednesday"
        const val thursday = "Thursday"
        const val friday = "Friday"
        const val saturday = "Saturday"
        const val sunday = "Sunday"
        const val emptyDay = ""
        const val invalidDay = "InvalidDay"
    }

    // ========== Durations ==========

    object Durations {
        val minutes30 = Duration(unit = "minutes", value = 30)
        val minutes45 = Duration(unit = "minutes", value = 45)
        val minutes60 = Duration(unit = "minutes", value = 60)
        val minutes90 = Duration(unit = "minutes", value = 90)
        val hours1 = Duration(unit = "hours", value = 1)
    }

    object DurationDtos {
        val minutes30 = DurationDto(unit = "minutes", value = 30)
        val minutes45 = DurationDto(unit = "minutes", value = 45)
        val minutes60 = DurationDto(unit = "minutes", value = 60)
        val minutes90 = DurationDto(unit = "minutes", value = 90)
        val hours1 = DurationDto(unit = "hours", value = 1)
    }

    // ========== Fitness Class DTOs ==========

    object FitnessClassDtos {
        val yoga = FitnessClassDto(
            dayOfWeek = DaysOfWeek.monday,
            description = "Relaxing yoga session for all levels",
            durationDto = DurationDtos.minutes60,
            endTime = "10:00",
            imageUrl = "https://example.com/yoga.jpg",
            name = "Morning Yoga",
            startTime = "09:00"
        )

        val spinning = FitnessClassDto(
            dayOfWeek = DaysOfWeek.monday,
            description = "High-intensity spinning workout",
            durationDto = DurationDtos.minutes45,
            endTime = "18:45",
            imageUrl = "https://example.com/spinning.jpg",
            name = "Evening Spin",
            startTime = "18:00"
        )

        val pilates = FitnessClassDto(
            dayOfWeek = DaysOfWeek.tuesday,
            description = "Core strength and flexibility",
            durationDto = DurationDtos.minutes45,
            endTime = "11:45",
            imageUrl = "https://example.com/pilates.jpg",
            name = "Pilates Power",
            startTime = "11:00"
        )

        val hiit = FitnessClassDto(
            dayOfWeek = DaysOfWeek.wednesday,
            description = "High-intensity interval training",
            durationDto = DurationDtos.minutes30,
            endTime = "19:30",
            imageUrl = "https://example.com/hiit.jpg",
            name = "HIIT Session",
            startTime = "19:00"
        )

        val zumba = FitnessClassDto(
            dayOfWeek = DaysOfWeek.thursday,
            description = "Dance fitness party",
            durationDto = DurationDtos.minutes60,
            endTime = "20:00",
            imageUrl = "https://example.com/zumba.jpg",
            name = "Zumba Night",
            startTime = "19:00"
        )

        val bootcamp = FitnessClassDto(
            dayOfWeek = DaysOfWeek.friday,
            description = "Military-style fitness training",
            durationDto = DurationDtos.minutes90,
            endTime = "07:30",
            imageUrl = "https://example.com/bootcamp.jpg",
            name = "Morning Bootcamp",
            startTime = "06:00"
        )
    }

    // ========== Fitness Classes (Domain) ==========

    object FitnessClasses {
        val yoga = FitnessClass(
            dayOfWeek = DaysOfWeek.monday,
            description = "Relaxing yoga session for all levels",
            duration = Durations.minutes60,
            endTime = "10:00",
            imageUrl = "https://example.com/yoga.jpg",
            name = "Morning Yoga",
            startTime = "09:00"
        )

        val spinning = FitnessClass(
            dayOfWeek = DaysOfWeek.monday,
            description = "High-intensity spinning workout",
            duration = Durations.minutes45,
            endTime = "18:45",
            imageUrl = "https://example.com/spinning.jpg",
            name = "Evening Spin",
            startTime = "18:00"
        )

        val pilates = FitnessClass(
            dayOfWeek = DaysOfWeek.tuesday,
            description = "Core strength and flexibility",
            duration = Durations.minutes45,
            endTime = "11:45",
            imageUrl = "https://example.com/pilates.jpg",
            name = "Pilates Power",
            startTime = "11:00"
        )

        val hiit = FitnessClass(
            dayOfWeek = DaysOfWeek.wednesday,
            description = "High-intensity interval training",
            duration = Durations.minutes30,
            endTime = "19:30",
            imageUrl = "https://example.com/hiit.jpg",
            name = "HIIT Session",
            startTime = "19:00"
        )

        val zumba = FitnessClass(
            dayOfWeek = DaysOfWeek.thursday,
            description = "Dance fitness party",
            duration = Durations.minutes60,
            endTime = "20:00",
            imageUrl = "https://example.com/zumba.jpg",
            name = "Zumba Night",
            startTime = "19:00"
        )

        val bootcamp = FitnessClass(
            dayOfWeek = DaysOfWeek.friday,
            description = "Military-style fitness training",
            duration = Durations.minutes90,
            endTime = "07:30",
            imageUrl = "https://example.com/bootcamp.jpg",
            name = "Morning Bootcamp",
            startTime = "06:00"
        )
    }

    // ========== Fitness Class Lists ==========

    object FitnessClassLists {
        val mondayClasses = listOf(
            FitnessClassDtos.yoga,
            FitnessClassDtos.spinning
        )

        val tuesdayClasses = listOf(FitnessClassDtos.pilates)

        val emptyList = emptyList<FitnessClassDto>()

        val mixedClasses = listOf(
            FitnessClassDtos.yoga,
            FitnessClassDtos.pilates,
            FitnessClassDtos.hiit
        )
    }

    object DomainFitnessClassLists {
        val mondayClasses = listOf(
            FitnessClasses.yoga,
            FitnessClasses.spinning
        )

        val tuesdayClasses = listOf(FitnessClasses.pilates)

        val emptyList = emptyList<FitnessClass>()

        val mixedClasses = listOf(
            FitnessClasses.yoga,
            FitnessClasses.pilates,
            FitnessClasses.hiit
        )
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("No classes found for the day")
        val timeout = Exception("Request timeout")
        val invalidDay = IllegalArgumentException("Invalid day of week")
    }
}

