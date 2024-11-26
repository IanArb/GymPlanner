package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.domain.AuthenticationMapper.toLoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.AuthenticationMapper.toRegister
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException

class AuthenticationRepository(
    private val remoteDataSource: AuthenticationRemoteDataSource,
) {

    suspend fun login(login: Login): Result<LoginResponse> {
        try {
            val result = remoteDataSource.login(login)
            return Result.success(result.toLoginResponse())
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
    }

    suspend fun register(register: Register): Result<RegisterResponse> {
        try {
            val result = remoteDataSource.register(register)
            return Result.success(result.toRegister())
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
    }
}