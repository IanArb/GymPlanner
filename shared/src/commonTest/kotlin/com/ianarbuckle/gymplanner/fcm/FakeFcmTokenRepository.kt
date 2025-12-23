package com.ianarbuckle.gymplanner.fcm

import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenRequest
import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenResponse

/**
 * Fake implementation of FcmTokenRepository for testing
 */
class FakeFcmTokenRepository(
    private val remoteDataSource: FcmTokenRemoteDataSource
) {

    suspend fun registerToken(fcmTokenRequest: FcmTokenRequest): Result<FcmTokenResponse> {
        return try {
            val response = remoteDataSource.registerToken(fcmTokenRequest)
            Result.success(response.toFcmTokenResponse())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}

