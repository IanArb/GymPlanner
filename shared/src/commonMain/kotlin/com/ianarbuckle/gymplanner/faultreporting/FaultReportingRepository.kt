package com.ianarbuckle.gymplanner.faultreporting

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FaultReportingRepository {
    suspend fun fetchFaultReports(): ApiResult<List<FaultReport>>

    suspend fun saveFaultReport(report: FaultReport): ApiResult<FaultReport>
}

class DefaultFaultReportingRepository :
    FaultReportingRepository,
    KoinComponent {
    private val remoteDataSource: DefaultFaultReportingRemoteDataSource by inject()

    override suspend fun fetchFaultReports(): ApiResult<List<FaultReport>> {
        try {
            val reports = remoteDataSource.reports()
            val faultReports = reports.map { it.toFaultReport() }
            return ApiResult.Success(faultReports)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return ApiResult.Failure(ex)
        }
    }

    override suspend fun saveFaultReport(report: FaultReport): ApiResult<FaultReport> {
        try {
            val faultReport = remoteDataSource.saveReport(report)
            return ApiResult.Success(faultReport.toFaultReport())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("FaultReportingRepository").e("Error saving fault report: $ex")
            return ApiResult.Failure(ex)
        }
    }
}
