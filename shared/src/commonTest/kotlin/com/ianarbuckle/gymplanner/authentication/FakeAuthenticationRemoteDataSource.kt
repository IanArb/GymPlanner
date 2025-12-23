package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.LoginResponses
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.RegisterResponses
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.dto.LoginResponseDto
import com.ianarbuckle.gymplanner.authentication.dto.RegisterResponseDto

/**
 * Fake implementation for testing AuthenticationRepository
 * Implements the AuthenticationRemoteDataSource interface
 */
class FakeAuthenticationRemoteDataSource : AuthenticationRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnLogin = false
    var shouldThrowExceptionOnRegister = false
    var loginException: Exception? = null
    var registerException: Exception? = null

    // Captured calls for verification
    val loginCalls = mutableListOf<Login>()
    val registerCalls = mutableListOf<Register>()

    // Configurable responses
    var loginResponse: LoginResponseDto = LoginResponses.default

    var registerResponse: RegisterResponseDto = RegisterResponses.default

    override suspend fun login(login: Login): LoginResponseDto {
        loginCalls.add(login)

        if (shouldThrowExceptionOnLogin) {
            throw loginException ?: RuntimeException("Login failed")
        }

        return loginResponse
    }

    override suspend fun register(register: Register): RegisterResponseDto {
        registerCalls.add(register)

        if (shouldThrowExceptionOnRegister) {
            throw registerException ?: RuntimeException("Registration failed")
        }

        return registerResponse
    }

    fun reset() {
        shouldThrowExceptionOnLogin = false
        shouldThrowExceptionOnRegister = false
        loginException = null
        registerException = null
        loginCalls.clear()
        registerCalls.clear()

        loginResponse = LoginResponses.default
        registerResponse = RegisterResponses.default
    }
}


