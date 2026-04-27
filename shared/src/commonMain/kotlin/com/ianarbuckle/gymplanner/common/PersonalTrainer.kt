package com.ianarbuckle.gymplanner.common

import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class PersonalTrainer(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val bio: String,
    val qualifications: List<String>,
    val socials: Map<String, String>,
    val gymLocation: GymLocation,
    val schedule: List<ScheduleSlot>? = null,
    val availabilityStatus: AvailabilityStatus? = null,
)

@Serializable
data class ScheduleSlot(val dayOfWeek: DayOfWeek, val startTime: LocalTime, val endTime: LocalTime)

enum class DayOfWeek {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
}

enum class AvailabilityStatus {
    AVAILABLE,
    UNAVAILABLE,
    UNKNOWN,
}
