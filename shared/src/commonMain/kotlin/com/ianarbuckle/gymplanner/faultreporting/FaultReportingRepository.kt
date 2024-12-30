package com.ianarbuckle.gymplanner.faultreporting

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReportMapper.toFaultReport
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FaultReportingRepository {
    suspend fun fetchFaultReports(): Result<List<FaultReport>>
    suspend fun saveFaultReport(report: FaultReport): Result<FaultReport>
}

class DefaultFaultReportingRepository : FaultReportingRepository, KoinComponent {

    private val remoteDataSource: FaultReportingRemoteDataSource by inject()

    override suspend fun fetchFaultReports(): Result<List<FaultReport>> {
        try {
            val reports = remoteDataSource.reports()
            val faultReports = reports.map {
                it.toFaultReport()
            }
            return Result.success(faultReports)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }

    override suspend fun saveFaultReport(report: FaultReport): Result<FaultReport> {
        try {
            val faultReport = remoteDataSource.saveReport(report)
            return Result.success(faultReport.toFaultReport())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("FaultReportingRepository").e("Error saving fault report: $ex")
            return Result.failure(ex)
        }
    }
}
