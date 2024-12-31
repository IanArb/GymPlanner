package com.ianarbuckle.gymplanner.faultreporting.domain

import com.ianarbuckle.gymplanner.faultreporting.dto.FaultReportDto

object FaultReportMapper {

    fun FaultReportDto.toFaultReport(): FaultReport {
        return FaultReport(
            description = description,
            photoUri = photoUri,
            machineNumber = machineNumber,
            date = date,
        )
    }
}
