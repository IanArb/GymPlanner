package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

/** Fake implementation of FaultReportingRepository for testing */
class FakeFaultReportingRepository(
    private val remoteDataSource: FaultReportingRemoteDataSource,
) : FaultReportingRepository {
    override suspend fun fetchFaultReports(): ApiResult<List<FaultReport>> = try {
        val reports = remoteDataSource.reports()
        val faultReports = reports.map { it.toFaultReport() }
        ApiResult.Success(faultReports)
    } catch (ex: Exception) {
        ApiResult.Failure(ex)
    }

    override suspend fun saveFaultReport(report: FaultReport): ApiResult<FaultReport> = try {
        val faultReport = remoteDataSource.saveReport(report)
        ApiResult.Success(faultReport.toFaultReport())
    } catch (ex: Exception) {
        ApiResult.Failure(ex)
    }
}
