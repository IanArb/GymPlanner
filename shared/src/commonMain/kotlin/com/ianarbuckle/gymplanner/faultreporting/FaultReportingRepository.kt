package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReportMapper.toFaultReport
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException


class FaultReportingRepository(private val remoteDataSource: FaultReportingRemoteDataSource) {

    suspend fun fetchFaultReports(): Result<List<FaultReport>> {
        try {
            val reports = remoteDataSource.reports()
            val faultReports = reports.map {
                it.toFaultReport()
            }
            return Result.success(faultReports)
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
    }

    suspend fun saveFaultReport(report: FaultReport): Result<FaultReport> {
        try {
            val faultReport = remoteDataSource.saveReport(report)
            return Result.success(faultReport.toFaultReport())
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
    }
}