package com.ianarbuckle.gymplanner.fcm

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenRequest
import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenResponse
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FcmTokenRepository() : KoinComponent {
    private val fcmTokenRemoteDataSource: FcmTokenRemoteDataSource by inject()

    suspend fun registerToken(fcmTokenRequest: FcmTokenRequest): Result<FcmTokenResponse> {
        return runCatching {
                fcmTokenRemoteDataSource.registerToken(fcmTokenRequest).toFcmTokenResponse()
            }
            .recoverCatching { exception ->
                if (exception is CancellationException) {
                    throw exception
                }
                Logger.withTag("FcmTokenRepository").e("Error registering fcm token: $exception")
                return Result.failure(exception)
            }
            .onSuccess { response ->
                Logger.withTag("FcmTokenRepository").d("Fcm token registered successfully")
                return Result.success(response)
            }
            .onFailure { throwable ->
                Logger.withTag("FcmTokenRepository").e("Error registering fcm token: $throwable")
                return Result.failure(throwable)
            }
    }
}
