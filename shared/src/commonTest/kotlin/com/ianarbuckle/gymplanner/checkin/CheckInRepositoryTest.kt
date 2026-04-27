package com.ianarbuckle.gymplanner.checkin

import com.ianarbuckle.gymplanner.checkin.CheckInTestDataProvider.CheckInResponseDtos
import com.ianarbuckle.gymplanner.checkin.CheckInTestDataProvider.CheckInTimes
import com.ianarbuckle.gymplanner.checkin.CheckInTestDataProvider.CheckIns
import com.ianarbuckle.gymplanner.checkin.CheckInTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.checkin.CheckInTestDataProvider.TrainerIds
import com.ianarbuckle.gymplanner.checkin.domain.CheckInStatus
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class CheckInRepositoryTest {

    private lateinit var repository: CheckInRepository
    private lateinit var fakeRemoteDataSource: FakeCheckInRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeCheckInRemoteDataSource()
        repository = FakeCheckInRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Check-In Success Tests ==========

    @Test
    fun `checkIn with valid data returns success with check-in response`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.onTime

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(CheckIns.onTime, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.checkInCalls.size)
    }

    @Test
    fun `checkIn calls remote data source with correct trainerId and request`() = runTest {
        // When
        repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertEquals(1, fakeRemoteDataSource.checkInCalls.size)
        val (capturedTrainerId, capturedRequest) = fakeRemoteDataSource.checkInCalls[0]
        assertEquals(TrainerIds.trainerOne, capturedTrainerId)
        assertEquals(CheckInTimes.onTime, capturedRequest.checkInTime)
    }

    @Test
    fun `checkIn maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.late

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.late)

        // Then
        val checkIn = result.getOrNull()
        assertNotNull(checkIn)
        assertEquals(CheckIns.late, checkIn)
    }

    @Test
    fun `checkIn with null id maps to domain model with null id`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.withoutId

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        val checkIn = result.getOrNull()
        assertNotNull(checkIn)
        assertNull(checkIn.id)
        assertEquals(CheckIns.withoutId, checkIn)
    }

    // ========== Status Mapping Tests ==========

    @Test
    fun `checkIn with ON_TIME status maps to ON_TIME domain status`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.onTime

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertEquals(CheckInStatus.ON_TIME, result.getOrNull()?.status)
    }

    @Test
    fun `checkIn with LATE status maps to LATE domain status`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.late

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.late)

        // Then
        assertEquals(CheckInStatus.LATE, result.getOrNull()?.status)
    }

    @Test
    fun `checkIn with UNKNOWN status maps to UNKNOWN domain status`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.unknownStatus

        // When
        val result = repository.checkIn(TrainerIds.trainerTwo, CheckInTimes.morning)

        // Then
        assertEquals(CheckInStatus.UNKNOWN, result.getOrNull()?.status)
    }

    // ========== Failure Tests ==========

    @Test
    fun `checkIn with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckIn = true
        fakeRemoteDataSource.checkInException = Exceptions.networkError

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `checkIn with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckIn = true
        fakeRemoteDataSource.checkInException = Exceptions.serverError

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `checkIn with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckIn = true
        fakeRemoteDataSource.checkInException = Exceptions.unauthorized

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `checkIn with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckIn = true
        fakeRemoteDataSource.checkInException = Exceptions.notFound

        // When
        val result = repository.checkIn(TrainerIds.nonExistentTrainer, CheckInTimes.onTime)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `checkIn handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckIn = true
        fakeRemoteDataSource.checkInException = Exceptions.validationError

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful checkIn does not throw exception`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertNull(exception, "Should not throw exception")
    }

    // ========== Repeated Calls Tests ==========

    @Test
    fun `multiple checkIn calls work independently`() = runTest {
        // When
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.onTime
        val result1 = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.late
        val result2 = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.late)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(CheckIns.onTime, result1.getOrNull())
        assertEquals(CheckIns.late, result2.getOrNull())
        assertEquals(2, fakeRemoteDataSource.checkInCalls.size)
    }

    @Test
    fun `checkIn calls for different trainers are captured separately`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.onTime

        // When
        repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)
        repository.checkIn(TrainerIds.trainerTwo, CheckInTimes.morning)

        // Then
        assertEquals(2, fakeRemoteDataSource.checkInCalls.size)
        assertEquals(TrainerIds.trainerOne, fakeRemoteDataSource.checkInCalls[0].first)
        assertEquals(TrainerIds.trainerTwo, fakeRemoteDataSource.checkInCalls[1].first)
    }

    // ========== Edge Cases ==========

    @Test
    fun `checkIn response contains all required fields`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.onTime

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        val checkIn = result.getOrNull()
        assertNotNull(checkIn)
        assertNotNull(checkIn.id)
        assertNotNull(checkIn.trainerId)
        assertNotNull(checkIn.checkInTime)
        assertNotNull(checkIn.status)
    }

    @Test
    fun `checkIn with empty trainerId still calls remote data source`() = runTest {
        // When
        val result = repository.checkIn("", CheckInTimes.onTime)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(1, fakeRemoteDataSource.checkInCalls.size)
        assertEquals("", fakeRemoteDataSource.checkInCalls[0].first)
    }

    @Test
    fun `checkIn preserves the checkInTime value through to response`() = runTest {
        // Given
        fakeRemoteDataSource.checkInResponse = CheckInResponseDtos.onTime

        // When
        val result = repository.checkIn(TrainerIds.trainerOne, CheckInTimes.onTime)

        // Then
        assertEquals(CheckInTimes.onTime, result.getOrNull()?.checkInTime)
    }
}
