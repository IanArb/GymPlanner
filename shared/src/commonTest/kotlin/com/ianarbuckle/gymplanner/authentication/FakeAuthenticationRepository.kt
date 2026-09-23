package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import com.ianarbuckle.gymplanner.common.ApiResult

class FakeAuthenticationRepository(
    private val remoteDataSource: AuthenticationRemoteDataSource,
) : AuthenticationRepository {
    override suspend fun login(login: Login): ApiResult<LoginResponse> = try {
        val result = remoteDataSource.login(login)
        ApiResult.Success(result.toLoginResponse())
    } catch (ex: Exception) {
        ApiResult.Failure(ex)
    }

    override suspend fun register(register: Register): ApiResult<RegisterResponse> = try {
        val result = remoteDataSource.register(register)
        ApiResult.Success(result.toRegister())
    } catch (ex: Exception) {
        ApiResult.Failure(ex)
    }
}
