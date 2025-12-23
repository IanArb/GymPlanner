package com.ianarbuckle.gymplanner.availability

import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.Availabilities
import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.AvailabilityDtos
import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.CheckAvailabilities
import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.CheckAvailabilityDtos
import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.Months
import com.ianarbuckle.gymplanner.availability.AvailabilityTestDataProvider.PersonalTrainerIds
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class AvailabilityRepositoryTest {

    private lateinit var repository: AvailabilityRepository
    private lateinit var fakeRemoteDataSource: FakeAvailabilityRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeAvailabilityRemoteDataSource()
        repository = FakeAvailabilityRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Fetch Availability Tests ==========

    @Test
    fun `fetchAvailability with valid trainer and month returns success with availability`() =
        runTest {
            // Given
            fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

            // When
            val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

            // Then
            assertTrue(result.isSuccess, "Result should be successful")
            assertEquals(Availabilities.januaryWithSlots, result.getOrNull())
            assertEquals(1, fakeRemoteDataSource.fetchAvailabilityCalls.size)
            assertEquals(
                PersonalTrainerIds.trainer1,
                fakeRemoteDataSource.fetchAvailabilityCalls[0].first,
            )
            assertEquals(Months.january, fakeRemoteDataSource.fetchAvailabilityCalls[0].second)
        }

    @Test
    fun `fetchAvailability calls remote data source with correct parameters`() = runTest {
        // When
        repository.getAvailability(PersonalTrainerIds.trainer2, Months.february)

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchAvailabilityCalls.size)
        val (trainerId, month) = fakeRemoteDataSource.fetchAvailabilityCalls[0]
        assertEquals(PersonalTrainerIds.trainer2, trainerId)
        assertEquals(Months.february, month)
    }

    @Test
    fun `fetchAvailability with multiple slots returns correct data structure`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertEquals(3, availability.slots.size)
        assertEquals(Availabilities.januaryWithSlots, availability)
    }

    @Test
    fun `fetchAvailability with single slot returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.februaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer2, Months.february)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertEquals(1, availability.slots.size)
        assertEquals(Availabilities.februaryWithSlots, availability)
    }

    @Test
    fun `fetchAvailability with no slots returns empty slots list`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.emptyAvailability

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer3, Months.march)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertTrue(availability.slots.isEmpty())
        assertEquals(Availabilities.emptyAvailability, availability)
    }

    @Test
    fun `fetchAvailability with slot containing no times is handled`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.withEmptySlot

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.december)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertEquals(1, availability.slots.size)
        assertTrue(availability.slots[0].times.isEmpty())
    }

    @Test
    fun `fetchAvailability with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchAvailability = true
        fakeRemoteDataSource.fetchAvailabilityException = Exceptions.networkError

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `fetchAvailability with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchAvailability = true
        fakeRemoteDataSource.fetchAvailabilityException = Exceptions.notFound

        // When
        val result =
            repository.getAvailability(PersonalTrainerIds.nonExistentTrainer, Months.january)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `fetchAvailability with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchAvailability = true
        fakeRemoteDataSource.fetchAvailabilityException = Exceptions.unauthorized

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `fetchAvailability with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchAvailability = true
        fakeRemoteDataSource.fetchAvailabilityException = Exceptions.serverError

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `fetchAvailability maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertEquals("avail-001", availability.id)
        assertEquals(Months.january, availability.month)
        assertEquals(PersonalTrainerIds.trainer1, availability.personalTrainerId)
        assertTrue(availability.slots.isNotEmpty())
    }

    @Test
    fun `fetchAvailability with different months works independently`() = runTest {
        // When
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots
        val result1 = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.februaryWithSlots
        val result2 = repository.getAvailability(PersonalTrainerIds.trainer2, Months.february)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(Months.january, result1.getOrNull()?.month)
        assertEquals(Months.february, result2.getOrNull()?.month)
        assertEquals(2, fakeRemoteDataSource.fetchAvailabilityCalls.size)
    }

    @Test
    fun `fetchAvailability returns immutable slots list`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertTrue(
            availability.slots::class.simpleName?.contains("Immutable") == true ||
                availability.slots::class.simpleName?.contains("Persistent") == true
        )
    }

    @Test
    fun `fetchAvailability returns immutable times list for each slot`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        availability.slots.forEach { slot ->
            assertTrue(
                slot.times::class.simpleName?.contains("Immutable") == true ||
                    slot.times::class.simpleName?.contains("Persistent") == true
            )
        }
    }

    // ========== Check Availability Tests ==========

    @Test
    fun `checkAvailability with available trainer returns true`() = runTest {
        // Given
        fakeRemoteDataSource.checkAvailabilityResponse = CheckAvailabilityDtos.available

        // When
        val result = repository.checkAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(CheckAvailabilities.available, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.checkAvailabilityCalls.size)
    }

    @Test
    fun `checkAvailability with unavailable trainer returns false`() = runTest {
        // Given
        fakeRemoteDataSource.checkAvailabilityResponse = CheckAvailabilityDtos.unavailable

        // When
        val result = repository.checkAvailability(PersonalTrainerIds.trainer2, Months.february)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(CheckAvailabilities.unavailable, result.getOrNull())
        assertEquals(false, result.getOrNull()?.isAvailable)
    }

    @Test
    fun `checkAvailability calls remote data source with correct parameters`() = runTest {
        // When
        repository.checkAvailability(PersonalTrainerIds.trainer3, Months.march)

        // Then
        assertEquals(1, fakeRemoteDataSource.checkAvailabilityCalls.size)
        val (trainerId, month) = fakeRemoteDataSource.checkAvailabilityCalls[0]
        assertEquals(PersonalTrainerIds.trainer3, trainerId)
        assertEquals(Months.march, month)
    }

    @Test
    fun `checkAvailability with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckAvailability = true
        fakeRemoteDataSource.checkAvailabilityException = Exceptions.networkError

        // When
        val result = repository.checkAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `checkAvailability with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckAvailability = true
        fakeRemoteDataSource.checkAvailabilityException = Exceptions.unauthorized

        // When
        val result = repository.checkAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `checkAvailability with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckAvailability = true
        fakeRemoteDataSource.checkAvailabilityException = Exceptions.notFound

        // When
        val result =
            repository.checkAvailability(PersonalTrainerIds.nonExistentTrainer, Months.january)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `checkAvailability maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.checkAvailabilityResponse = CheckAvailabilityDtos.trainer3Available

        // When
        val result = repository.checkAvailability(PersonalTrainerIds.trainer3, Months.january)

        // Then
        val checkAvailability = result.getOrNull()
        assertNotNull(checkAvailability)
        assertEquals(PersonalTrainerIds.trainer3, checkAvailability.personalTrainerId)
        assertTrue(checkAvailability.isAvailable)
    }

    @Test
    fun `checkAvailability with different trainers works independently`() = runTest {
        // When
        fakeRemoteDataSource.checkAvailabilityResponse = CheckAvailabilityDtos.available
        val result1 = repository.checkAvailability(PersonalTrainerIds.trainer1, Months.january)

        fakeRemoteDataSource.checkAvailabilityResponse = CheckAvailabilityDtos.unavailable
        val result2 = repository.checkAvailability(PersonalTrainerIds.trainer2, Months.february)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(true, result1.getOrNull()?.isAvailable)
        assertEquals(false, result2.getOrNull()?.isAvailable)
        assertEquals(2, fakeRemoteDataSource.checkAvailabilityCalls.size)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `fetchAvailability handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchAvailability = true
        fakeRemoteDataSource.fetchAvailabilityException = Exceptions.invalidMonth

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.invalidMonth)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `checkAvailability handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnCheckAvailability = true
        fakeRemoteDataSource.checkAvailabilityException = Exceptions.invalidTrainerId

        // When
        val result = repository.checkAvailability("", Months.january)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful fetchAvailability does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `successful checkAvailability does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.checkAvailability(PersonalTrainerIds.trainer1, Months.january)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `fetchAvailability with empty trainer ID calls remote data source`() = runTest {
        // When
        repository.getAvailability("", Months.january)

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchAvailabilityCalls.size)
        assertEquals("", fakeRemoteDataSource.fetchAvailabilityCalls[0].first)
    }

    @Test
    fun `fetchAvailability with empty month calls remote data source`() = runTest {
        // When
        repository.getAvailability(PersonalTrainerIds.trainer1, "")

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchAvailabilityCalls.size)
        assertEquals("", fakeRemoteDataSource.fetchAvailabilityCalls[0].second)
    }

    @Test
    fun `checkAvailability with empty trainer ID calls remote data source`() = runTest {
        // When
        repository.checkAvailability("", Months.january)

        // Then
        assertEquals(1, fakeRemoteDataSource.checkAvailabilityCalls.size)
        assertEquals("", fakeRemoteDataSource.checkAvailabilityCalls[0].first)
    }

    @Test
    fun `fetchAvailability response contains all required fields`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        assertNotNull(availability.id)
        assertNotNull(availability.month)
        assertNotNull(availability.personalTrainerId)
        assertNotNull(availability.slots)
    }

    @Test
    fun `checkAvailability response contains all required fields`() = runTest {
        // Given
        fakeRemoteDataSource.checkAvailabilityResponse = CheckAvailabilityDtos.available

        // When
        val result = repository.checkAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val checkAvailability = result.getOrNull()
        assertNotNull(checkAvailability)
        assertNotNull(checkAvailability.personalTrainerId)
        assertNotNull(checkAvailability.isAvailable)
    }

    @Test
    fun `fetchAvailability slots contain proper time structure`() = runTest {
        // Given
        fakeRemoteDataSource.fetchAvailabilityResponse = AvailabilityDtos.januaryWithSlots

        // When
        val result = repository.getAvailability(PersonalTrainerIds.trainer1, Months.january)

        // Then
        val availability = result.getOrNull()
        assertNotNull(availability)
        availability.slots.forEach { slot ->
            assertNotNull(slot.id)
            assertNotNull(slot.date)
            assertNotNull(slot.times)
            slot.times.forEach { time ->
                assertNotNull(time.id)
                assertNotNull(time.startTime)
                assertNotNull(time.endTime)
                assertNotNull(time.status)
            }
        }
    }
}
