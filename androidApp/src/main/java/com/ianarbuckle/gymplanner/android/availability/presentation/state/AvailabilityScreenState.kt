package com.ianarbuckle.gymplanner.android.availability.presentation.state

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

data class AvailabilityScreenState(
    val personalTrainer: PersonalTrainer,
)

data class Client(
    val userId: String,
    val firstName: String,
    val surname: String,
    val email: String,
    val gymLocation: GymLocation,
)

data class PersonalTrainer(
    val personalTrainerId: String,
    val name: String,
    val imageUrl: String,
    val gymLocation: String,
    val qualifications: List<String>,
)
