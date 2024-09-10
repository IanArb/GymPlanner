package com.ianarbuckle.gymplanner.model

data class Client(
    val id: String,
    val firstName: String,
    val surname: String,
    val strengthLevel: String,
    val gymPlan: GymPlan?,
)

data class GymPlan(
    val name: String,
    val personalTrainer: PersonalTrainer,
    val startDate: String,
    val endDate: String,
    val sessions: List<Session>,
)

data class PersonalTrainer(
    val id: String,
    val name: String,
    val socials: Map<String, String>
)

data class Session(
    val name: String,
    val workouts: List<Workout>
)

data class Workout(
    val name: String,
    val sets: Int,
    val repetitions: Int,
    val weight: Float,
    val note: String,
)