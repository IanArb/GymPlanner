package com.ianarbuckle.gymplanner.faultreporting.dto

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import kotlinx.serialization.Serializable

@Serializable
data class FaultReportDto(
    val id: String,
    val machineNumber: Int,
    val description: String,
    val photoUri: String,
    val date: String,
) {

    fun toFaultReport(): FaultReport =
        FaultReport(
            description = description,
            photoUri = photoUri,
            machineNumber = machineNumber,
            date = date,
        )
}
