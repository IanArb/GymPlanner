package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.FaultReportingTestDataProvider.DomainReportLists
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingTestDataProvider.FaultReportDtos
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingTestDataProvider.FaultReports
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingTestDataProvider.ReportLists
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class FaultReportingRepositoryTest {

    private lateinit var repository: FaultReportingRepository
    private lateinit var fakeRemoteDataSource: FakeFaultReportingRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeFaultReportingRemoteDataSource()
        repository = FakeFaultReportingRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Fetch Fault Reports Tests ==========

    @Test
    fun `fetchFaultReports with multiple reports returns success with list`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.multipleReports

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(DomainReportLists.multipleReports, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.reportsCalls.size)
    }

    @Test
    fun `fetchFaultReports calls remote data source`() = runTest {
        // When
        repository.fetchFaultReports()

        // Then
        assertEquals(1, fakeRemoteDataSource.reportsCalls.size)
    }

    @Test
    fun `fetchFaultReports with multiple reports returns correct count`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.multipleReports

        // When
        val result = repository.fetchFaultReports()

        // Then
        val reports = result.getOrNull()
        assertNotNull(reports)
        assertEquals(3, reports.size)
    }

    @Test
    fun `fetchFaultReports with single report returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.singleReport

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainReportLists.singleReport, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `fetchFaultReports with no reports returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.emptyList

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainReportLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `fetchFaultReports with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnReports = true
        fakeRemoteDataSource.reportsException = Exceptions.networkError

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `fetchFaultReports with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnReports = true
        fakeRemoteDataSource.reportsException = Exceptions.serverError

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `fetchFaultReports with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnReports = true
        fakeRemoteDataSource.reportsException = Exceptions.unauthorized

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `fetchFaultReports maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.multipleReports

        // When
        val result = repository.fetchFaultReports()

        // Then
        val reports = result.getOrNull()
        assertNotNull(reports)
        assertEquals(3, reports.size)
        assertEquals("Treadmill belt is slipping and making grinding noise", reports[0].description)
        assertEquals(101, reports[0].machineNumber)
        assertEquals("2025-12-23", reports[0].date)
    }

    @Test
    fun `fetchFaultReports preserves report order`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.multipleReports

        // When
        val result = repository.fetchFaultReports()

        // Then
        val reports = result.getOrNull()
        assertNotNull(reports)
        assertEquals(FaultReports.treadmillFault, reports[0])
        assertEquals(FaultReports.bikeFault, reports[1])
        assertEquals(FaultReports.rowerFault, reports[2])
    }

    @Test
    fun `multiple fetchFaultReports calls work independently`() = runTest {
        // When
        fakeRemoteDataSource.reportsResponse = ReportLists.multipleReports
        val result1 = repository.fetchFaultReports()

        fakeRemoteDataSource.reportsResponse = ReportLists.singleReport
        val result2 = repository.fetchFaultReports()

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(3, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
        assertEquals(2, fakeRemoteDataSource.reportsCalls.size)
    }

    // ========== Save Fault Report Tests ==========

    @Test
    fun `saveFaultReport with valid report returns success`() = runTest {
        // Given
        fakeRemoteDataSource.saveReportResponse = FaultReportDtos.savedReport

        // When
        val result = repository.saveFaultReport(FaultReports.newReport)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(FaultReports.ellipticalFault, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.saveReportCalls.size)
    }

    @Test
    fun `saveFaultReport calls remote data source with correct data`() = runTest {
        // When
        repository.saveFaultReport(FaultReports.treadmillFault)

        // Then
        assertEquals(1, fakeRemoteDataSource.saveReportCalls.size)
        val savedReport = fakeRemoteDataSource.saveReportCalls[0]
        assertEquals(FaultReports.treadmillFault, savedReport)
        assertEquals(
            "Treadmill belt is slipping and making grinding noise",
            savedReport.description,
        )
        assertEquals(101, savedReport.machineNumber)
    }

    @Test
    fun `saveFaultReport maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.saveReportResponse = FaultReportDtos.savedReport

        // When
        val result = repository.saveFaultReport(FaultReports.newReport)

        // Then
        val savedReport = result.getOrNull()
        assertNotNull(savedReport)
        assertEquals("Elliptical left pedal is loose and wobbling", savedReport.description)
        assertEquals(404, savedReport.machineNumber)
        assertEquals("2025-12-23", savedReport.date)
    }

    @Test
    fun `saveFaultReport with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveReport = true
        fakeRemoteDataSource.saveReportException = Exceptions.networkError

        // When
        val result = repository.saveFaultReport(FaultReports.treadmillFault)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `saveFaultReport with save failed error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveReport = true
        fakeRemoteDataSource.saveReportException = Exceptions.saveFailed

        // When
        val result = repository.saveFaultReport(FaultReports.bikeFault)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.saveFailed, result.exceptionOrNull())
    }

    @Test
    fun `saveFaultReport with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveReport = true
        fakeRemoteDataSource.saveReportException = Exceptions.unauthorized

        // When
        val result = repository.saveFaultReport(FaultReports.treadmillFault)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `saveFaultReport with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveReport = true
        fakeRemoteDataSource.saveReportException = Exceptions.serverError

        // When
        val result = repository.saveFaultReport(FaultReports.rowerFault)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `multiple saveFaultReport calls work independently`() = runTest {
        // When
        val result1 = repository.saveFaultReport(FaultReports.treadmillFault)
        val result2 = repository.saveFaultReport(FaultReports.bikeFault)
        val result3 = repository.saveFaultReport(FaultReports.rowerFault)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeRemoteDataSource.saveReportCalls.size)
    }

    @Test
    fun `saveFaultReport with empty description is handled`() = runTest {
        // When
        val result = repository.saveFaultReport(FaultReports.emptyDescriptionReport)

        // Then
        assertTrue(result.isSuccess)
        val savedReport = fakeRemoteDataSource.saveReportCalls[0]
        assertEquals("", savedReport.description)
    }

    @Test
    fun `saveFaultReport with long description is handled`() = runTest {
        // When
        val result = repository.saveFaultReport(FaultReports.longDescriptionReport)

        // Then
        assertTrue(result.isSuccess)
        val savedReport = fakeRemoteDataSource.saveReportCalls[0]
        assertTrue(savedReport.description.length > 100)
    }

    @Test
    fun `saveFaultReport with invalid machine number is handled`() = runTest {
        // When
        val result = repository.saveFaultReport(FaultReports.invalidMachineReport)

        // Then
        assertTrue(result.isSuccess)
        val savedReport = fakeRemoteDataSource.saveReportCalls[0]
        assertEquals(-1, savedReport.machineNumber)
    }

    // ========== Integration Tests ==========

    @Test
    fun `fetchFaultReports followed by saveFaultReport works correctly`() = runTest {
        // When
        val fetchResult = repository.fetchFaultReports()
        val saveResult = repository.saveFaultReport(FaultReports.newReport)

        // Then
        assertTrue(fetchResult.isSuccess)
        assertTrue(saveResult.isSuccess)
        assertEquals(1, fakeRemoteDataSource.reportsCalls.size)
        assertEquals(1, fakeRemoteDataSource.saveReportCalls.size)
    }

    @Test
    fun `saveFaultReport followed by fetchFaultReports works correctly`() = runTest {
        // When
        val saveResult = repository.saveFaultReport(FaultReports.treadmillFault)
        val fetchResult = repository.fetchFaultReports()

        // Then
        assertTrue(saveResult.isSuccess)
        assertTrue(fetchResult.isSuccess)
        assertEquals(1, fakeRemoteDataSource.saveReportCalls.size)
        assertEquals(1, fakeRemoteDataSource.reportsCalls.size)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `fetchFaultReports handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnReports = true
        fakeRemoteDataSource.reportsException = Exceptions.timeout

        // When
        val result = repository.fetchFaultReports()

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `saveFaultReport handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnSaveReport = true
        fakeRemoteDataSource.saveReportException = Exceptions.validationError

        // When
        val result = repository.saveFaultReport(FaultReports.treadmillFault)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful fetchFaultReports does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.fetchFaultReports()
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `successful saveFaultReport does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.saveFaultReport(FaultReports.treadmillFault)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `fetchFaultReports response contains all report fields`() = runTest {
        // Given
        fakeRemoteDataSource.reportsResponse = ReportLists.multipleReports

        // When
        val result = repository.fetchFaultReports()

        // Then
        val reports = result.getOrNull()
        assertNotNull(reports)
        reports.forEach { report ->
            assertNotNull(report.description)
            assertNotNull(report.photoUri)
            assertNotNull(report.machineNumber)
            assertNotNull(report.date)
        }
    }

    @Test
    fun `saveFaultReport preserves all report fields`() = runTest {
        // Given
        val originalReport = FaultReports.treadmillFault

        // When
        repository.saveFaultReport(originalReport)

        // Then
        val savedReport = fakeRemoteDataSource.saveReportCalls[0]
        assertEquals(originalReport.description, savedReport.description)
        assertEquals(originalReport.photoUri, savedReport.photoUri)
        assertEquals(originalReport.machineNumber, savedReport.machineNumber)
        assertEquals(originalReport.date, savedReport.date)
    }

    @Test
    fun `saveFaultReport with different machine numbers works independently`() = runTest {
        // When
        val result1 = repository.saveFaultReport(FaultReports.treadmillFault)
        val result2 = repository.saveFaultReport(FaultReports.bikeFault)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeRemoteDataSource.saveReportCalls.size)
        assertEquals(101, fakeRemoteDataSource.saveReportCalls[0].machineNumber)
        assertEquals(202, fakeRemoteDataSource.saveReportCalls[1].machineNumber)
    }
}
