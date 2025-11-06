package com.ianarbuckle.gymplanner.fcm.domain

import kotlinx.serialization.Serializable

@Serializable data class FcmTokenRequest(val userId: String, val token: String)
