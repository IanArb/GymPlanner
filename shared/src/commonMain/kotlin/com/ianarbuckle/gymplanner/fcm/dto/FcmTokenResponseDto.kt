package com.ianarbuckle.gymplanner.fcm.dto

import com.ianarbuckle.gymplanner.fcm.domain.FcmTokenResponse
import kotlinx.serialization.Serializable

@Serializable
data class FcmTokenResponseDto(val token: String? = null) {

    fun toFcmTokenResponse() = FcmTokenResponse(token = token)
}
