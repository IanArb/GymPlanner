package com.ianarbuckle.gymplanner.authentication.dto

import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    val username: String,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
)

@Serializable
data class RegisterResponseDto(val message: String) {

    fun toRegister(): RegisterResponse = RegisterResponse(message)
}
