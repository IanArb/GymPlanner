package com.ianarbuckle.gymplanner.model

import kotlinx.serialization.Serializable

@Serializable
data class FaultReport(
    val description: String,
    val photoUri: String,
    val machineNumber: Int,
    val date: String,
)