package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassTestDataProvider.DaysOfWeek
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassTestDataProvider.DomainFitnessClassLists
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassTestDataProvider.FitnessClassLists
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassTestDataProvider.FitnessClasses
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class FitnessClassRepositoryTest {

    private lateinit var repository: FakeFitnessClassRepository
    private lateinit var fakeRemoteDataSource: FakeFitnessClassRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeFitnessClassRemoteDataSource()
        repository = FakeFitnessClassRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Get Fitness Classes Tests ==========

    @Test
    fun `getFitnessClasses with Monday returns success with classes list`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mondayClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(DomainFitnessClassLists.mondayClasses, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.fitnessClassesCalls.size)
        assertEquals(DaysOfWeek.monday, fakeRemoteDataSource.fitnessClassesCalls[0])
    }

    @Test
    fun `getFitnessClasses calls remote data source with correct day`() = runTest {
        // When
        repository.getFitnessClasses(DaysOfWeek.tuesday)

        // Then
        assertEquals(1, fakeRemoteDataSource.fitnessClassesCalls.size)
        assertEquals(DaysOfWeek.tuesday, fakeRemoteDataSource.fitnessClassesCalls[0])
    }

    @Test
    fun `getFitnessClasses with multiple classes returns correct count`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mondayClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertEquals(2, classes.size)
    }

    @Test
    fun `getFitnessClasses with single class returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.tuesdayClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.tuesday)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainFitnessClassLists.tuesdayClasses, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `getFitnessClasses with no classes returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.emptyList

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.sunday)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainFitnessClassLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getFitnessClasses with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFitnessClasses = true
        fakeRemoteDataSource.fitnessClassesException = Exceptions.networkError

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getFitnessClasses with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFitnessClasses = true
        fakeRemoteDataSource.fitnessClassesException = Exceptions.serverError

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.tuesday)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `getFitnessClasses with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFitnessClasses = true
        fakeRemoteDataSource.fitnessClassesException = Exceptions.unauthorized

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.wednesday)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `getFitnessClasses with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFitnessClasses = true
        fakeRemoteDataSource.fitnessClassesException = Exceptions.notFound

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.sunday)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `getFitnessClasses maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mondayClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertEquals(2, classes.size)
        assertEquals("Morning Yoga", classes[0].name)
        assertEquals("09:00", classes[0].startTime)
        assertEquals(60, classes[0].duration.value)
        assertEquals("minutes", classes[0].duration.unit)
    }

    @Test
    fun `getFitnessClasses with different days works independently`() = runTest {
        // When
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mondayClasses
        val result1 = repository.getFitnessClasses(DaysOfWeek.monday)

        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.tuesdayClasses
        val result2 = repository.getFitnessClasses(DaysOfWeek.tuesday)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
        assertEquals(2, fakeRemoteDataSource.fitnessClassesCalls.size)
        assertEquals(DaysOfWeek.monday, fakeRemoteDataSource.fitnessClassesCalls[0])
        assertEquals(DaysOfWeek.tuesday, fakeRemoteDataSource.fitnessClassesCalls[1])
    }

    @Test
    fun `getFitnessClasses preserves class order`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mondayClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertEquals(FitnessClasses.yoga, classes[0])
        assertEquals(FitnessClasses.spinning, classes[1])
    }

    @Test
    fun `multiple getFitnessClasses calls work independently`() = runTest {
        // When
        val result1 = repository.getFitnessClasses(DaysOfWeek.monday)
        val result2 = repository.getFitnessClasses(DaysOfWeek.tuesday)
        val result3 = repository.getFitnessClasses(DaysOfWeek.wednesday)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeRemoteDataSource.fitnessClassesCalls.size)
    }

    @Test
    fun `getFitnessClasses response contains all class fields`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mondayClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        classes.forEach { fitnessClass ->
            assertNotNull(fitnessClass.name)
            assertNotNull(fitnessClass.dayOfWeek)
            assertNotNull(fitnessClass.description)
            assertNotNull(fitnessClass.startTime)
            assertNotNull(fitnessClass.endTime)
            assertNotNull(fitnessClass.imageUrl)
            assertNotNull(fitnessClass.duration)
            assertNotNull(fitnessClass.duration.unit)
            assertNotNull(fitnessClass.duration.value)
        }
    }

    @Test
    fun `getFitnessClasses maps duration correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fitnessClassesResponse = FitnessClassLists.mixedClasses

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertEquals(60, classes[0].duration.value) // Yoga
        assertEquals("minutes", classes[0].duration.unit)
        assertEquals(45, classes[1].duration.value) // Pilates
        assertEquals(30, classes[2].duration.value) // HIIT
    }

    // ========== Edge Case Tests ==========

    @Test
    fun `getFitnessClasses with empty day is handled`() = runTest {
        // When
        val result = repository.getFitnessClasses(DaysOfWeek.emptyDay)

        // Then
        assertEquals(1, fakeRemoteDataSource.fitnessClassesCalls.size)
        assertEquals("", fakeRemoteDataSource.fitnessClassesCalls[0])
    }

    @Test
    fun `getFitnessClasses with invalid day is handled`() = runTest {
        // When
        val result = repository.getFitnessClasses(DaysOfWeek.invalidDay)

        // Then
        assertEquals(1, fakeRemoteDataSource.fitnessClassesCalls.size)
        assertEquals("InvalidDay", fakeRemoteDataSource.fitnessClassesCalls[0])
    }

    @Test
    fun `getFitnessClasses with all days of week works correctly`() = runTest {
        // When
        repository.getFitnessClasses(DaysOfWeek.monday)
        repository.getFitnessClasses(DaysOfWeek.tuesday)
        repository.getFitnessClasses(DaysOfWeek.wednesday)
        repository.getFitnessClasses(DaysOfWeek.thursday)
        repository.getFitnessClasses(DaysOfWeek.friday)
        repository.getFitnessClasses(DaysOfWeek.saturday)
        repository.getFitnessClasses(DaysOfWeek.sunday)

        // Then
        assertEquals(7, fakeRemoteDataSource.fitnessClassesCalls.size)
        assertEquals(DaysOfWeek.monday, fakeRemoteDataSource.fitnessClassesCalls[0])
        assertEquals(DaysOfWeek.sunday, fakeRemoteDataSource.fitnessClassesCalls[6])
    }

    @Test
    fun `getFitnessClasses with different durations is handled`() = runTest {
        // Given - Classes with 30, 45, 60, 90 minute durations
        fakeRemoteDataSource.fitnessClassesResponse =
            listOf(
                FitnessClassTestDataProvider.FitnessClassDtos.hiit, // 30 min
                FitnessClassTestDataProvider.FitnessClassDtos.spinning, // 45 min
                FitnessClassTestDataProvider.FitnessClassDtos.yoga, // 60 min
                FitnessClassTestDataProvider.FitnessClassDtos.bootcamp, // 90 min
            )

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertEquals(4, classes.size)
        assertEquals(30, classes[0].duration.value)
        assertEquals(45, classes[1].duration.value)
        assertEquals(60, classes[2].duration.value)
        assertEquals(90, classes[3].duration.value)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `getFitnessClasses handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFitnessClasses = true
        fakeRemoteDataSource.fitnessClassesException = Exceptions.timeout

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `getFitnessClasses handles invalid day exception gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFitnessClasses = true
        fakeRemoteDataSource.fitnessClassesException = Exceptions.invalidDay

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.invalidDay)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful getFitnessClasses does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getFitnessClasses(DaysOfWeek.monday)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Time-based Tests ==========

    @Test
    fun `getFitnessClasses with morning classes is handled`() = runTest {
        // Given - Classes before noon
        fakeRemoteDataSource.fitnessClassesResponse =
            listOf(
                FitnessClassTestDataProvider.FitnessClassDtos.yoga,
                FitnessClassTestDataProvider.FitnessClassDtos.bootcamp,
            )

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertTrue(classes[0].startTime < "12:00")
        assertTrue(classes[1].startTime < "12:00")
    }

    @Test
    fun `getFitnessClasses with evening classes is handled`() = runTest {
        // Given - Classes after 5 PM
        fakeRemoteDataSource.fitnessClassesResponse =
            listOf(
                FitnessClassTestDataProvider.FitnessClassDtos.spinning,
                FitnessClassTestDataProvider.FitnessClassDtos.hiit,
            )

        // When
        val result = repository.getFitnessClasses(DaysOfWeek.monday)

        // Then
        val classes = result.getOrNull()
        assertNotNull(classes)
        assertTrue(classes[0].startTime >= "17:00")
        assertTrue(classes[1].startTime >= "17:00")
    }
}
