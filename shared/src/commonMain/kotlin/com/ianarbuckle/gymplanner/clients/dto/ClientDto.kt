package com.ianarbuckle.gymplanner.clients.dto

import com.ianarbuckle.gymplanner.common.AvailabilityStatus
import com.ianarbuckle.gymplanner.common.DayOfWeek
import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.common.PersonalTrainer
import com.ianarbuckle.gymplanner.common.ScheduleSlot
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class ClientDto(
    val id: String,
    val firstName: String,
    val surname: String,
    val strengthLevel: String,
    val gymPlan: GymPlanDto?,
)

@Serializable
data class GymPlanDto(
    val name: String,
    val personalTrainer: PersonalTrainerDto,
    val startDate: String,
    val endDate: String,
    val sessions: List<SessionDto>,
)

@Serializable
data class PersonalTrainerDto(
    val id: String? = null,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val bio: String,
    val socials: Map<String, String>? = null,
    val qualifications: List<String>,
    val gymLocation: GymLocationDto,
    val schedule: List<ScheduleSlotDto>? = null,
    val availabilityStatus: AvailabilityStatusDto? = null,
) {

    fun toPersonalTrainer(): PersonalTrainer =
        PersonalTrainer(
            id = id,
            firstName = firstName,
            lastName = lastName,
            imageUrl = imageUrl,
            bio = bio,
            socials = socials ?: emptyMap(),
            qualifications = qualifications,
            gymLocation = gymLocation.toGymLocation(),
            schedule = schedule?.map { it.toScheduleSlot() },
            availabilityStatus = availabilityStatus?.toAvailabilityStatus(),
        )

    private fun GymLocationDto.toGymLocation(): GymLocation =
        this.let {
            when (it) {
                GymLocationDto.CLONTARF -> GymLocation.CLONTARF
                GymLocationDto.ASTONQUAY -> GymLocation.ASTONQUAY
                GymLocationDto.LEOPARDSTOWN -> GymLocation.LEOPARDSTOWN
                GymLocationDto.DUNLOAGHAIRE -> GymLocation.DUNLOAGHAIRE
                GymLocationDto.SANDYMOUNT -> GymLocation.SANDYMOUNT
                GymLocationDto.WESTMANSTOWN -> GymLocation.WESTMANSTOWN
                GymLocationDto.UNKNOWN -> GymLocation.UNKNOWN
            }
        }

    private fun ScheduleSlotDto.toScheduleSlot(): ScheduleSlot =
        ScheduleSlot(
            dayOfWeek = this.dayOfWeek.toDayOfWeek(),
            startTime = this.startTime,
            endTime = this.endTime,
        )

    private fun DayOfWeekDto.toDayOfWeek(): DayOfWeek =
        when (this) {
            DayOfWeekDto.MONDAY -> DayOfWeek.MONDAY
            DayOfWeekDto.TUESDAY -> DayOfWeek.TUESDAY
            DayOfWeekDto.WEDNESDAY -> DayOfWeek.WEDNESDAY
            DayOfWeekDto.THURSDAY -> DayOfWeek.THURSDAY
            DayOfWeekDto.FRIDAY -> DayOfWeek.FRIDAY
            DayOfWeekDto.SATURDAY -> DayOfWeek.SATURDAY
            DayOfWeekDto.SUNDAY -> DayOfWeek.SUNDAY
        }

    private fun AvailabilityStatusDto.toAvailabilityStatus(): AvailabilityStatus =
        when (this) {
            AvailabilityStatusDto.AVAILABLE -> AvailabilityStatus.AVAILABLE
            AvailabilityStatusDto.UNAVAILABLE -> AvailabilityStatus.UNAVAILABLE
            else -> AvailabilityStatus.UNKNOWN
        }
}

@Serializable
data class ScheduleSlotDto(
    val dayOfWeek: DayOfWeekDto,
    val startTime: LocalTime,
    val endTime: LocalTime,
)

enum class DayOfWeekDto {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
}

enum class AvailabilityStatusDto {
    AVAILABLE,
    UNAVAILABLE,
    UNKNOWN,
}

enum class GymLocationDto {
    CLONTARF,
    ASTONQUAY,
    LEOPARDSTOWN,
    DUNLOAGHAIRE,
    SANDYMOUNT,
    WESTMANSTOWN,
    UNKNOWN,
}

@Serializable data class SessionDto(val name: String, val workouts: List<WorkoutDto>)

@Serializable
data class WorkoutDto(
    val name: String,
    val sets: Int,
    val repetitions: Int,
    val weight: WeightDto,
    val note: String,
)

@Serializable data class WeightDto(val value: Double, val unit: String)
