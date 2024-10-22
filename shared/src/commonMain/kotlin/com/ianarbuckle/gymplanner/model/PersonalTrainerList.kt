package com.ianarbuckle.gymplanner.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonalTrainerList(
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val bio: String,
    val socials: Map<String, String>,
    val gymLocation: GymLocation,
)

enum class GymLocation {
    CLONTARF,
    ASTONQUAY,
    LEOPARDSTOWN,
    DUNLOAGHAIRE,
    WESTMANSTOWN,
    UNKNOWN,
}