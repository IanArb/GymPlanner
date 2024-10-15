package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.data.faultreporting.dto.FaultReportDto
import com.ianarbuckle.gymplanner.model.FaultReport

object FaultReportMapper {

    fun FaultReportDto.toFaultReport(): FaultReport {
        return FaultReport(
            description = description,
            photoUri = photoUri,
            machineNumber = machineNumber,
            date = date
        )
    }
}