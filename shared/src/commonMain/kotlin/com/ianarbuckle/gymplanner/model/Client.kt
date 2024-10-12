package com.ianarbuckle.gymplanner.model

data class Client(
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
    val firstName: String,
    val surname: String,
    val socials: Map<String, String>
)

data class Session(
    val name: String,
    val workout: List<Workout>
)

data class Workout(
    val name: String,
    val sets: Int,
    val repetitions: Int,
    val weight: Weight,
    val note: String,
)

data class Weight(
    val value: Double,
    val unit: String,
)

