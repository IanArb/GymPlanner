package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport

/** Fake implementation of FaultReportingRepository for testing */
class FakeFaultReportingRepository(
    private val remoteDataSource: FaultReportingRemoteDataSource,
) : FaultReportingRepository {
    override suspend fun fetchFaultReports(): Result<List<FaultReport>> = try {
        val reports = remoteDataSource.reports()
        val faultReports = reports.map { it.toFaultReport() }
        Result.success(faultReports)
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun saveFaultReport(report: FaultReport): Result<FaultReport> = try {
        val faultReport = remoteDataSource.saveReport(report)
        Result.success(faultReport.toFaultReport())
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
