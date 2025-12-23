package com.ianarbuckle.gymplanner.fcm

import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenRequest
import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenResponse
import com.ianarbuckle.gymplanner.fcm.dto.FcmTokenResponseDto

/** Provides test data for FcmTokenRepository tests */
object FcmTokenTestDataProvider {

    // ========== User IDs ==========

    object UserIds {
        const val user1 = "user-001"
        const val user2 = "user-002"
        const val user3 = "user-003"
        const val emptyUserId = ""
    }

    // ========== FCM Tokens ==========

    object FcmTokens {
        const val validToken1 = "fcm-token-abc123def456ghi789jkl012mno345pqr678stu901vwx234yz"
        const val validToken2 = "fcm-token-987zyx654wvu321tsr098qpo765nml432kjh109ihg876fed543cba"
        const val validToken3 = "fcm-token-111aaa222bbb333ccc444ddd555eee666fff777ggg888hhh999iii"
        const val expiredToken = "fcm-token-expired-old-invalid-token"
        const val emptyToken = ""
        val longToken = "fcm-token-${"a".repeat(200)}"
    }

    // ========== FCM Token Requests ==========

    object FcmTokenRequests {
        val user1Request = FcmTokenRequest(userId = UserIds.user1, token = FcmTokens.validToken1)

        val user2Request = FcmTokenRequest(userId = UserIds.user2, token = FcmTokens.validToken2)

        val user3Request = FcmTokenRequest(userId = UserIds.user3, token = FcmTokens.validToken3)

        val emptyUserIdRequest =
            FcmTokenRequest(userId = UserIds.emptyUserId, token = FcmTokens.validToken1)

        val emptyTokenRequest =
            FcmTokenRequest(userId = UserIds.user1, token = FcmTokens.emptyToken)

        val expiredTokenRequest =
            FcmTokenRequest(userId = UserIds.user1, token = FcmTokens.expiredToken)

        val longTokenRequest = FcmTokenRequest(userId = UserIds.user1, token = FcmTokens.longToken)
    }

    // ========== FCM Token Response DTOs ==========

    object FcmTokenResponseDtos {
        val successResponse = FcmTokenResponseDto(token = FcmTokens.validToken1)

        val user2Response = FcmTokenResponseDto(token = FcmTokens.validToken2)

        val nullTokenResponse = FcmTokenResponseDto(token = null)

        val emptyTokenResponse = FcmTokenResponseDto(token = "")
    }

    // ========== FCM Token Responses (Domain) ==========

    object FcmTokenResponses {
        val successResponse = FcmTokenResponse(token = FcmTokens.validToken1)

        val user2Response = FcmTokenResponse(token = FcmTokens.validToken2)

        val nullTokenResponse = FcmTokenResponse(token = null)

        val emptyTokenResponse = FcmTokenResponse(token = "")
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val invalidToken = IllegalArgumentException("Invalid FCM token format")
        val timeout = Exception("Request timeout")
        val registrationFailed = RuntimeException("FCM token registration failed")
        val tokenExpired = RuntimeException("FCM token has expired")
    }
}
