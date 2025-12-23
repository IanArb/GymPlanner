package com.ianarbuckle.gymplanner.fcm

import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenRequest
import com.ianarbuckle.gymplanner.fcm.dto.FcmTokenResponseDto

/**
 * Fake implementation for testing FcmTokenRepository
 * Implements the FcmTokenRemoteDataSource interface
 */
class FakeFcmTokenRemoteDataSource : FcmTokenRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnRegisterToken = false
    var registerTokenException: Exception? = null

    // Captured calls for verification
    val registerTokenCalls = mutableListOf<FcmTokenRequest>()

    // Configurable responses
    var registerTokenResponse: FcmTokenResponseDto = FcmTokenTestDataProvider.FcmTokenResponseDtos.successResponse

    override suspend fun registerToken(fcmTokenRequest: FcmTokenRequest): FcmTokenResponseDto {
        registerTokenCalls.add(fcmTokenRequest)

        if (shouldThrowExceptionOnRegisterToken) {
            throw registerTokenException ?: RuntimeException("Register token failed")
        }

        return registerTokenResponse
    }

    fun reset() {
        shouldThrowExceptionOnRegisterToken = false
        registerTokenException = null
        registerTokenCalls.clear()

        registerTokenResponse = FcmTokenTestDataProvider.FcmTokenResponseDtos.successResponse
    }
}

