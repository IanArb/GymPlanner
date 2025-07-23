package com.ianarbuckle.gymplanner.authentication

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AuthenticationRepository {
    suspend fun login(login: Login): Result<LoginResponse>

    suspend fun register(register: Register): Result<RegisterResponse>
}

class DefaultAuthenticationRepository : AuthenticationRepository, KoinComponent {

    private val remoteDataSource: AuthenticationRemoteDataSource by inject()

    override suspend fun login(login: Login): Result<LoginResponse> {
        try {
            val result = remoteDataSource.login(login)
            return Result.success(result.toLoginResponse())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("AuthenticationRepository").e("Error logging in user: $ex")
            return Result.failure(ex)
        }
    }

    override suspend fun register(register: Register): Result<RegisterResponse> {
        try {
            val result = remoteDataSource.register(register)
            return Result.success(result.toRegister())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.e("Error registering user: $ex")
            return Result.failure(ex)
        }
    }
}
