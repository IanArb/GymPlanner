package com.ianarbuckle.gymplanner.android.reporting.fakes

import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

class FakeFaultRepository : FaultReportingRepository {
    var shouldReturnError = false

    override suspend fun fetchFaultReports(): ApiResult<List<FaultReport>> = if (shouldReturnError) {
        ApiResult.Failure(Exception("Error"))
    } else {
        ApiResult.Success(listOf(mockFaultReport()))
    }

    override suspend fun saveFaultReport(report: FaultReport): ApiResult<FaultReport> = if (shouldReturnError) {
        ApiResult.Failure(Exception("Error"))
    } else {
        ApiResult.Success(mockFaultReport())
    }

    private fun mockFaultReport(): FaultReport = FaultReport(
        machineNumber = 123,
        description = "Machine broken",
        photoUri = "uri",
        date = "2021-09-01",
    )
}
