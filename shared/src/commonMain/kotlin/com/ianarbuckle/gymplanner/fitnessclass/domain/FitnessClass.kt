package com.ianarbuckle.gymplanner.fitnessclass.domain

data class FitnessClass(
    val dayOfWeek: String,
    val description: String,
    val duration: Duration,
    val endTime: String,
    val imageUrl: String,
    val name: String,
    val startTime: String,
)

data class Duration(val unit: String, val value: Int)
