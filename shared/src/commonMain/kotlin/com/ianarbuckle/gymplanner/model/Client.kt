package com.ianarbuckle.gymplanner.model

import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val id: String,
    val firstName: String,
    val surname: String,
    val strengthLevel: String,
    val gymPlan: GymPlan?,
)

@Serializable
data class GymPlan(
    val name: String,
    val personalTrainer: PersonalTrainer,
    val startDate: String,
    val endDate: String,
    val sessions: List<Session>,
)

@Serializable
data class PersonalTrainer(
    val id: String? = null,
    val firstName: String,
    val surname: String,
    val socials: Map<String, String>
)

@Serializable
data class Session(
    val name: String,
    val workouts: List<Workout>
)

@Serializable
data class Workout(
    val name: String,
    val sets: Int,
    val repetitions: Int,
    val weight: Weight,
    val note: String,
)

@Serializable
data class Weight(
    val value: Double,
    val unit: String,
)