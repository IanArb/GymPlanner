package com.ianarbuckle.gymplanner.authentication.dto

import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import kotlinx.serialization.Serializable

@Serializable data class LoginDto(val username: String, val password: String)

@Serializable
data class LoginResponseDto(val userId: String, val token: String, val expiration: Long) {

    fun toLoginResponse(): LoginResponse =
        LoginResponse(userId = userId, token = token, expiration = expiration)
}
