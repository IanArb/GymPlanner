package com.ianarbuckle.gymplanner.authentication.domain

import com.ianarbuckle.gymplanner.authentication.dto.LoginDto
import com.ianarbuckle.gymplanner.authentication.dto.LoginResponseDto
import com.ianarbuckle.gymplanner.authentication.dto.RegisterDto
import com.ianarbuckle.gymplanner.authentication.dto.RegisterResponseDto

object AuthenticationMapper {

    fun LoginDto.toLogin(): Login {
        return Login(username, password)
    }

    fun LoginResponseDto.toLoginResponse(): LoginResponse {
        return LoginResponse(
            userId = userId,
            token = token,
            expiration = expiration,
        )
    }

    fun RegisterDto.toRegister(): Register {
        return Register(
            username = username,
            password = password,
            email = email,
            firstName = firstName,
            lastName = lastName,
        )
    }

    fun RegisterResponseDto.toRegister(): RegisterResponse {
        return RegisterResponse(message)
    }
}
