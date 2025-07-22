package com.ianarbuckle.gymplanner.faultreporting.dto

import kotlinx.serialization.Serializable

@Serializable
data class FaultReportDto(
    val id: String,
    val machineNumber: Int,
    val description: String,
    val photoUri: String,
    val date: String,
)
