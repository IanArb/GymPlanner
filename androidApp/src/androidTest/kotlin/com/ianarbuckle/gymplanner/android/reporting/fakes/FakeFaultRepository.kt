package com.ianarbuckle.gymplanner.android.reporting.fakes

import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

class FakeFaultRepository : FaultReportingRepository {

    var shouldReturnError = false

    override suspend fun fetchFaultReports(): Result<List<FaultReport>> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            Result.success(listOf(mockFaultReport()))
        }
    }

    override suspend fun saveFaultReport(report: FaultReport): Result<FaultReport> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            Result.success(mockFaultReport())
        }
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
