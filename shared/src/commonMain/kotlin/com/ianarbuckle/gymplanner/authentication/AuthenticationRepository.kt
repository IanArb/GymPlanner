package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.domain.AuthenticationMapper.toLoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.AuthenticationMapper.toRegister
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse

class AuthenticationRepository(
    private val remoteDataSource: AuthenticationRemoteDataSource,
) {

    suspend fun login(login: Login): Result<LoginResponse> {
        try {
            val result = remoteDataSource.login(login)
            return Result.success(result.toLoginResponse())
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    suspend fun register(register: Register): Result<RegisterResponse> {
        try {
            val result = remoteDataSource.register(register)
            return Result.success(result.toRegister())
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }
}