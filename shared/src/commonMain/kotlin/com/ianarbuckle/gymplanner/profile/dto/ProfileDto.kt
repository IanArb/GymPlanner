package com.ianarbuckle.gymplanner.profile.dto

import com.ianarbuckle.gymplanner.profile.domain.Profile
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val userId: String,
    val username: String,
    val firstName: String,
    val surname: String,
    val email: String,
) {

    fun toProfile(): Profile =
        Profile(
            userId = userId,
            username = username,
            firstName = firstName,
            surname = surname,
            email = email,
        )
}
