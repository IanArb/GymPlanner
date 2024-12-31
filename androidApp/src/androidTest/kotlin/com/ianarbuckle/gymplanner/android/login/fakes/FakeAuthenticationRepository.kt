package com.ianarbuckle.gymplanner.android.login.fakes

import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse

class FakeAuthenticationRepository : AuthenticationRepository {

    override suspend fun login(login: Login): Result<LoginResponse> {
        return if (login.username == "invaliduser" && login.password == "wrongpassword") {
            Result.failure(Exception("Error logging in. Please try again."))
        } else {
            mockLoginApiSuccess()
        }
    }

    override suspend fun register(register: Register): Result<RegisterResponse> {
        return mockRegisterApiSuccess()
    }

    private fun mockLoginApiSuccess(): Result<LoginResponse> {
        return Result.success(
            LoginResponse(
                "6730e1cb37f4352118e0c8e1",
                "BNcSBPga34TCOk0FLJdTFvsp1RPi5RQ1K2quumdKsqOXv5yRqN7Ebm3aPHC1nqpfqNB2cGQdukJkIyPx0SmAxg",
                10000,
            ),
        )
    }

    private fun mockLoginApiFailure(): Result<LoginResponse> {
        return Result.failure(
            Exception("Error"),
        )
    }

    private fun mockRegisterApiSuccess(): Result<RegisterResponse> {
        return Result.success(
            RegisterResponse(
                message = "Registered",
            ),
        )
    }

    private fun mockRegisterApiFailure(): Result<RegisterResponse> {
        return Result.failure(
            Exception("error"),
        )
    }
}
