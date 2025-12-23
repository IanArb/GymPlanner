package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import com.ianarbuckle.gymplanner.authentication.dto.LoginResponseDto
import com.ianarbuckle.gymplanner.authentication.dto.RegisterResponseDto

/** Provides test data for AuthenticationRepository tests */
object AuthenticationTestDataProvider {

    // ========== Login Test Data ==========

    object Logins {
        val valid = Login(username = "testuser", password = "password123")
        val invalid = Login(username = "wronguser", password = "wrongpass")
        val empty = Login(username = "", password = "")
        val withSpecialChars = Login(username = "user@#$%", password = "p@ssw0rd!#$%^&*()")
        val user1 = Login(username = "user1", password = "pass1")
        val user2 = Login(username = "user2", password = "pass2")
        val mapper = Login(username = "mapper", password = "test123")
    }

    object LoginResponses {
        val default =
            LoginResponseDto(
                userId = "test-user-id",
                token = "test-token",
                expiration = 1234567890L,
            )

        val valid =
            LoginResponseDto(
                userId = "user123",
                token = "jwt-token-abc",
                expiration = 1735689600000L,
            )

        val mapped =
            LoginResponseDto(
                userId = "mapped-user",
                token = "mapped-token",
                expiration = 9999999999L,
            )

        val user2Response =
            LoginResponseDto(userId = "user2-id", token = "user2-token", expiration = 8888888888L)

        val zeroExpiration = LoginResponseDto(userId = "id", token = "token", expiration = 0L)

        val negativeExpiration = LoginResponseDto(userId = "id", token = "token", expiration = -1L)
    }

    object LoginDomainResponses {
        val valid =
            LoginResponse(userId = "user123", token = "jwt-token-abc", expiration = 1735689600000L)

        val mapped =
            LoginResponse(userId = "mapped-user", token = "mapped-token", expiration = 9999999999L)
    }

    // ========== Register Test Data ==========

    object Registers {
        val valid =
            Register(
                username = "newuser",
                password = "securepass123",
                email = "user@example.com",
                firstName = "John",
                lastName = "Doe",
            )

        val duplicate =
            Register(
                username = "existinguser",
                password = "pass123",
                email = "existing@example.com",
                firstName = "Jane",
                lastName = "Smith",
            )

        val networkError =
            Register(
                username = "testuser",
                password = "password",
                email = "test@test.com",
                firstName = "Test",
                lastName = "User",
            )

        val mapper =
            Register(
                username = "mapper",
                password = "test",
                email = "mapper@test.com",
                firstName = "Map",
                lastName = "Per",
            )

        val complete =
            Register(
                username = "completeuser",
                password = "CompletePass123!",
                email = "complete@example.com",
                firstName = "Complete",
                lastName = "User",
            )

        val minimal =
            Register(
                username = "min",
                password = "p",
                email = "m@e.c",
                firstName = "F",
                lastName = "L",
            )

        val withSpecialChars =
            Register(
                username = "user_2024",
                password = "P@ssw0rd!",
                email = "user+test@example.com",
                firstName = "Jean-Pierre",
                lastName = "O'Brien",
            )

        val user1 = Register("user1", "pass1", "u1@e.c", "F1", "L1")
        val user2 = Register("user2", "pass2", "u2@e.c", "F2", "L2")

        val generic = Register("u", "p", "e", "f", "l")
        val genericWithEmail = Register("u", "p", "e@e.c", "f", "l")
    }

    object RegisterResponses {
        val default = RegisterResponseDto(message = "Registration successful")

        val success = RegisterResponseDto(message = "User registered successfully")

        val customSuccess = RegisterResponseDto(message = "Custom success message")

        val secondRegistration = RegisterResponseDto(message = "Second registration successful")
    }

    object RegisterDomainResponses {
        val success = RegisterResponse(message = "User registered successfully")

        val customSuccess = RegisterResponse(message = "Custom success message")
    }

    // ========== Exceptions ==========

    object Exceptions {
        val invalidCredentials = RuntimeException("Invalid credentials")
        val networkUnavailable = Exception("Network unavailable")
        val connectionTimeout = Exception("Connection timeout")
        val unexpectedError = IllegalStateException("Unexpected error")
        val invalidInput = IllegalArgumentException("Invalid input")
        val usernameTaken = RuntimeException("Username already exists")
    }
}
