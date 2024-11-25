package com.ianarbuckle.gymplanner.profile.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val userId: String,
    val username: String,
    val firstName: String,
    val surname: String,
    val email: String,
)
