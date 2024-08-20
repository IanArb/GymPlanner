package com.example.gymplanner.model

data class GymPlan(
    val id: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val workouts: List<Workout>,
)

data class Workout(
    val name: String,
    val sets: Int,
    val repetitions: Int,
    val weight: Float,
    val note: String,
)

data class Session(
    val startTime: String,
    val endTime: String,
)