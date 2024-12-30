package com.ianarbuckle.gymplanner.profile.domain

data class Profile(
    val userId: String,
    val username: String,
    val firstName: String,
    val surname: String,
    val email: String,
)
