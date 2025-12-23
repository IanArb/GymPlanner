package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.dto.FaultReportDto

/**
 * Fake implementation for testing FaultReportingRepository Implements the
 * FaultReportingRemoteDataSource interface
 */
class FakeFaultReportingRemoteDataSource : FaultReportingRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnReports = false
    var shouldThrowExceptionOnSaveReport = false
    var reportsException: Exception? = null
    var saveReportException: Exception? = null

    // Captured calls for verification
    val reportsCalls = mutableListOf<Unit>()
    val saveReportCalls = mutableListOf<FaultReport>()

    // Configurable responses
    var reportsResponse: List<FaultReportDto> =
        FaultReportingTestDataProvider.ReportLists.multipleReports
    var saveReportResponse: FaultReportDto =
        FaultReportingTestDataProvider.FaultReportDtos.savedReport

    override suspend fun reports(): List<FaultReportDto> {
        reportsCalls.add(Unit)

        if (shouldThrowExceptionOnReports) {
            throw reportsException ?: RuntimeException("Fetch reports failed")
        }

        return reportsResponse
    }

    override suspend fun saveReport(report: FaultReport): FaultReportDto {
        saveReportCalls.add(report)

        if (shouldThrowExceptionOnSaveReport) {
            throw saveReportException ?: RuntimeException("Save report failed")
        }

        return saveReportResponse
    }

    fun reset() {
        shouldThrowExceptionOnReports = false
        shouldThrowExceptionOnSaveReport = false
        reportsException = null
        saveReportException = null
        reportsCalls.clear()
        saveReportCalls.clear()

        reportsResponse = FaultReportingTestDataProvider.ReportLists.multipleReports
        saveReportResponse = FaultReportingTestDataProvider.FaultReportDtos.savedReport
    }
}
