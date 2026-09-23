package com.ianarbuckle.gymplanner.android.login.fakes

import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import com.ianarbuckle.gymplanner.common.ApiResult

class FakeAuthenticationRepository : AuthenticationRepository {
    override suspend fun login(login: Login): ApiResult<LoginResponse> =
        if (login.username == "invaliduser" && login.password == "wrongpassword") {
            ApiResult.Failure(Exception("Error logging in. Please try again."))
        } else {
            mockLoginApiSuccess()
        }

    override suspend fun register(register: Register): ApiResult<RegisterResponse> =
        mockRegisterApiSuccess()

    private fun mockLoginApiSuccess(): ApiResult<LoginResponse> = ApiResult.Success(
        LoginResponse(
            "6730e1cb37f4352118e0c8e1",
            "BNcSBPga34TCOk0FLJdTFvsp1RPi5RQ1K2quumdKsqOXv5yRqN7Ebm3aPHC1nqpfqNB2cGQdukJkIyPx0SmAxg",
            10000,
        ),
    )

    private fun mockRegisterApiSuccess(): ApiResult<RegisterResponse> =
        ApiResult.Success(RegisterResponse(message = "Registered"))
}