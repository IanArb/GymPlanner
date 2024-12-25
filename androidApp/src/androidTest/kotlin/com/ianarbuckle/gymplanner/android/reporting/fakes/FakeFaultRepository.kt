package com.ianarbuckle.gymplanner.android.reporting.fakes

import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

class FakeFaultRepository : FaultReportingRepository {

    override suspend fun fetchFaultReports(): Result<List<FaultReport>> {
        return Result.success(listOf(mockFaultReport()))
    }

    override suspend fun saveFaultReport(report: FaultReport): Result<FaultReport> {
        if (report.description == "Failure") {
            return Result.failure(Exception("Error"))
        }
        return Result.success(mockFaultReport())
    }

    private fun mockFaultReport(): FaultReport {
        return FaultReport(
            machineNumber = 123,
            description = "Machine broken",
            photoUri = "uri",
            date = "2021-09-01",
        )
    }
}