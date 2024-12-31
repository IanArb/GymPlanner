package com.ianarbuckle.gymplanner.clients.dto

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
)

enum class GymLocationDto {
    CLONTARF,
    ASTONQUAY,
    LEOPARDSTOWN,
    DUNLOAGHAIRE,
    SANDYMOUNT,
    WESTMANSTOWN,
    UNKNOWN,
}

@Serializable
data class SessionDto(
    val name: String,
    val workouts: List<WorkoutDto>,
)

@Serializable
data class WorkoutDto(
    val name: String,
    val sets: Int,
    val repetitions: Int,
    val weight: WeightDto,
    val note: String,
)

@Serializable
data class WeightDto(
    val value: Double,
    val unit: String,
)
