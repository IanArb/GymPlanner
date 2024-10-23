package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReportMapper.toFaultReport


class FaultReportingRepository(private val remoteDataSource: FaultReportingRemoteDataSource) {

    suspend fun fetchFaultReports(): Result<List<FaultReport>> {
        try {
            val reports = remoteDataSource.reports()
            val faultReports = reports.map {
                it.toFaultReport()
            }
            return Result.success(faultReports)
        } catch (ex: Exception) {
            return Result.failure(ex)
        }
    }

    suspend fun saveFaultReport(report: FaultReport): Result<FaultReport> {
        try {
            val faultReport = remoteDataSource.saveReport(report)
            return Result.success(faultReport.toFaultReport())
        } catch (ex: Exception) {
            return Result.failure(ex)
        }
    }
}