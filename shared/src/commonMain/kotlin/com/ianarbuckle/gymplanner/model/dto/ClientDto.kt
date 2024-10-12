package com.ianarbuckle.gymplanner.model.dto

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
    val surname: String,
    val socials: Map<String, String>
)

@Serializable
data class SessionDto(
    val name: String,
    val workouts: List<WorkoutDto>
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