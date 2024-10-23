package com.ianarbuckle.gymplanner.faultreporting.domain

import kotlinx.serialization.Serializable

@Serializable
data class FaultReport(
    val description: String,
    val photoUri: String,
    val machineNumber: Int,
    val date: String,
)