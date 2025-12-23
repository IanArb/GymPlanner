package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse

class FakeAuthenticationRepository(private val remoteDataSource: AuthenticationRemoteDataSource) :
    AuthenticationRepository {

    override suspend fun login(login: Login): Result<LoginResponse> {
        return try {
            val result = remoteDataSource.login(login)
            Result.success(result.toLoginResponse())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun register(register: Register): Result<RegisterResponse> {
        return try {
            val result = remoteDataSource.register(register)
            Result.success(result.toRegister())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
