package com.ianarbuckle.gymplanner.authentication.domain

import kotlinx.serialization.Serializable

@Serializable data class Login(val username: String, val password: String)

data class LoginResponse(val userId: String, val token: String, val expiration: Long)
