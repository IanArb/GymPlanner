package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersTestDataProvider.DomainPersonalTrainerLists
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersTestDataProvider.PersonalTrainerDtos
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersTestDataProvider.PersonalTrainerLists
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersTestDataProvider.PersonalTrainers
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersTestDataProvider.TrainerIds
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class PersonalTrainersRepositoryTest {

    private lateinit var repository: FakePersonalTrainersRepository
    private lateinit var fakeRemoteDataSource: FakePersonalTrainersRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakePersonalTrainersRemoteDataSource()
        repository = FakePersonalTrainersRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Get Personal Trainers Tests ==========

    @Test
    fun `getPersonalTrainers with Clontarf location returns success with trainers list`() =
        runTest {
            // Given
            fakeRemoteDataSource.fetchPersonalTrainersResponse =
                PersonalTrainerLists.clontarfTrainers

            // When
            val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

            // Then
            assertTrue(result.isSuccess, "Result should be successful")
            assertEquals(DomainPersonalTrainerLists.clontarfTrainers, result.getOrNull())
            assertEquals(1, fakeRemoteDataSource.fetchPersonalTrainersCalls.size)
            assertEquals(GymLocation.CLONTARF, fakeRemoteDataSource.fetchPersonalTrainersCalls[0])
        }

    @Test
    fun `getPersonalTrainers calls remote data source with correct location`() = runTest {
        // When
        repository.getPersonalTrainers(GymLocation.ASTONQUAY)

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchPersonalTrainersCalls.size)
        assertEquals(GymLocation.ASTONQUAY, fakeRemoteDataSource.fetchPersonalTrainersCalls[0])
    }

    @Test
    fun `getPersonalTrainers with multiple trainers returns correct count`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.allTrainers

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        val trainers = result.getOrNull()
        assertNotNull(trainers)
        assertEquals(6, trainers.size)
    }

    @Test
    fun `getPersonalTrainers with single trainer returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.clontarfTrainers

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainPersonalTrainerLists.clontarfTrainers, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `getPersonalTrainers with no trainers returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.emptyList

        // When
        val result = repository.getPersonalTrainers(GymLocation.UNKNOWN)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainPersonalTrainerLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getPersonalTrainers with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchPersonalTrainers = true
        fakeRemoteDataSource.fetchPersonalTrainersException = Exceptions.networkError

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainers with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchPersonalTrainers = true
        fakeRemoteDataSource.fetchPersonalTrainersException = Exceptions.serverError

        // When
        val result = repository.getPersonalTrainers(GymLocation.ASTONQUAY)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainers with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchPersonalTrainers = true
        fakeRemoteDataSource.fetchPersonalTrainersException = Exceptions.unauthorized

        // When
        val result = repository.getPersonalTrainers(GymLocation.LEOPARDSTOWN)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainers maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.clontarfTrainers

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        val trainers = result.getOrNull()
        assertNotNull(trainers)
        assertEquals(1, trainers.size)
        assertEquals("Sarah", trainers[0].firstName)
        assertEquals("Murphy", trainers[0].lastName)
        assertEquals(GymLocation.CLONTARF, trainers[0].gymLocation)
        assertEquals(3, trainers[0].qualifications.size)
    }

    @Test
    fun `getPersonalTrainers with different locations works independently`() = runTest {
        // When
        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.clontarfTrainers
        val result1 = repository.getPersonalTrainers(GymLocation.CLONTARF)

        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.astonQuayTrainers
        val result2 = repository.getPersonalTrainers(GymLocation.ASTONQUAY)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(1, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
        assertEquals(2, fakeRemoteDataSource.fetchPersonalTrainersCalls.size)
        assertEquals(GymLocation.CLONTARF, fakeRemoteDataSource.fetchPersonalTrainersCalls[0])
        assertEquals(GymLocation.ASTONQUAY, fakeRemoteDataSource.fetchPersonalTrainersCalls[1])
    }

    @Test
    fun `getPersonalTrainers preserves trainer order`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse =
            listOf(PersonalTrainerDtos.sarah, PersonalTrainerDtos.john, PersonalTrainerDtos.emma)

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        val trainers = result.getOrNull()
        assertNotNull(trainers)
        assertEquals(PersonalTrainers.sarah, trainers[0])
        assertEquals(PersonalTrainers.john, trainers[1])
        assertEquals(PersonalTrainers.emma, trainers[2])
    }

    @Test
    fun `multiple getPersonalTrainers calls work independently`() = runTest {
        // When
        val result1 = repository.getPersonalTrainers(GymLocation.CLONTARF)
        val result2 = repository.getPersonalTrainers(GymLocation.ASTONQUAY)
        val result3 = repository.getPersonalTrainers(GymLocation.LEOPARDSTOWN)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeRemoteDataSource.fetchPersonalTrainersCalls.size)
    }

    @Test
    fun `getPersonalTrainers response contains all trainer fields`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = PersonalTrainerLists.allTrainers

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        val trainers = result.getOrNull()
        assertNotNull(trainers)
        trainers.forEach { trainer ->
            assertNotNull(trainer.id)
            assertNotNull(trainer.firstName)
            assertNotNull(trainer.lastName)
            assertNotNull(trainer.imageUrl)
            assertNotNull(trainer.bio)
            assertNotNull(trainer.socials)
            assertNotNull(trainer.qualifications)
            assertNotNull(trainer.gymLocation)
        }
    }

    @Test
    fun `getPersonalTrainers with all gym locations is handled`() = runTest {
        // When
        repository.getPersonalTrainers(GymLocation.CLONTARF)
        repository.getPersonalTrainers(GymLocation.ASTONQUAY)
        repository.getPersonalTrainers(GymLocation.LEOPARDSTOWN)
        repository.getPersonalTrainers(GymLocation.SANDYMOUNT)
        repository.getPersonalTrainers(GymLocation.DUNLOAGHAIRE)
        repository.getPersonalTrainers(GymLocation.WESTMANSTOWN)

        // Then
        assertEquals(6, fakeRemoteDataSource.fetchPersonalTrainersCalls.size)
    }

    @Test
    fun `getPersonalTrainers with null socials maps to empty map`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = listOf(PersonalTrainerDtos.michael)

        // When
        val result = repository.getPersonalTrainers(GymLocation.SANDYMOUNT)

        // Then
        val trainer = result.getOrNull()?.first()
        assertNotNull(trainer)
        assertEquals(PersonalTrainers.michael, trainer)
        assertTrue(trainer.socials.isEmpty())
    }

    // ========== Get Personal Trainer By ID Tests ==========

    @Test
    fun `getPersonalTrainerById with valid ID returns success with trainer`() = runTest {
        // Given
        fakeRemoteDataSource.findPersonalTrainerByIdResponse = PersonalTrainerDtos.sarah

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.trainer1)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(PersonalTrainers.sarah, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.findPersonalTrainerByIdCalls.size)
        assertEquals(TrainerIds.trainer1, fakeRemoteDataSource.findPersonalTrainerByIdCalls[0])
    }

    @Test
    fun `getPersonalTrainerById calls remote data source with correct ID`() = runTest {
        // When
        repository.getPersonalTrainerById(TrainerIds.trainer2)

        // Then
        assertEquals(1, fakeRemoteDataSource.findPersonalTrainerByIdCalls.size)
        assertEquals(TrainerIds.trainer2, fakeRemoteDataSource.findPersonalTrainerByIdCalls[0])
    }

    @Test
    fun `getPersonalTrainerById maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.findPersonalTrainerByIdResponse = PersonalTrainerDtos.john

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.trainer2)

        // Then
        val trainer = result.getOrNull()
        assertNotNull(trainer)
        assertEquals("John", trainer.firstName)
        assertEquals("O'Brien", trainer.lastName)
        assertEquals(GymLocation.ASTONQUAY, trainer.gymLocation)
    }

    @Test
    fun `getPersonalTrainerById with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindPersonalTrainerById = true
        fakeRemoteDataSource.findPersonalTrainerByIdException = Exceptions.networkError

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.trainer1)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainerById with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindPersonalTrainerById = true
        fakeRemoteDataSource.findPersonalTrainerByIdException = Exceptions.notFound

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.nonExistent)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainerById with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindPersonalTrainerById = true
        fakeRemoteDataSource.findPersonalTrainerByIdException = Exceptions.unauthorized

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.trainer1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainerById with different trainers works independently`() = runTest {
        // When
        fakeRemoteDataSource.findPersonalTrainerByIdResponse = PersonalTrainerDtos.sarah
        val result1 = repository.getPersonalTrainerById(TrainerIds.trainer1)

        fakeRemoteDataSource.findPersonalTrainerByIdResponse = PersonalTrainerDtos.john
        val result2 = repository.getPersonalTrainerById(TrainerIds.trainer2)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(PersonalTrainers.sarah, result1.getOrNull())
        assertEquals(PersonalTrainers.john, result2.getOrNull())
        assertEquals(2, fakeRemoteDataSource.findPersonalTrainerByIdCalls.size)
    }

    @Test
    fun `multiple getPersonalTrainerById calls work independently`() = runTest {
        // When
        val result1 = repository.getPersonalTrainerById(TrainerIds.trainer1)
        val result2 = repository.getPersonalTrainerById(TrainerIds.trainer2)
        val result3 = repository.getPersonalTrainerById(TrainerIds.trainer3)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeRemoteDataSource.findPersonalTrainerByIdCalls.size)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `getPersonalTrainers handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchPersonalTrainers = true
        fakeRemoteDataSource.fetchPersonalTrainersException = Exceptions.timeout

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `getPersonalTrainerById handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFindPersonalTrainerById = true
        fakeRemoteDataSource.findPersonalTrainerByIdException = Exceptions.timeout

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.trainer1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `successful getPersonalTrainers does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getPersonalTrainers(GymLocation.CLONTARF)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `successful getPersonalTrainerById does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getPersonalTrainerById(TrainerIds.trainer1)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `getPersonalTrainerById with empty ID is handled`() = runTest {
        // When
        val result = repository.getPersonalTrainerById(TrainerIds.emptyId)

        // Then
        assertEquals(1, fakeRemoteDataSource.findPersonalTrainerByIdCalls.size)
        assertEquals("", fakeRemoteDataSource.findPersonalTrainerByIdCalls[0])
    }

    @Test
    fun `getPersonalTrainers preserves qualifications list`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = listOf(PersonalTrainerDtos.sarah)

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        val trainer = result.getOrNull()?.first()
        assertNotNull(trainer)
        assertEquals(3, trainer.qualifications.size)
        assertTrue(trainer.qualifications.contains("NASM-CPT"))
        assertTrue(trainer.qualifications.contains("Precision Nutrition Level 1"))
        assertTrue(trainer.qualifications.contains("CrossFit Level 2"))
    }

    @Test
    fun `getPersonalTrainers preserves socials map`() = runTest {
        // Given
        fakeRemoteDataSource.fetchPersonalTrainersResponse = listOf(PersonalTrainerDtos.sarah)

        // When
        val result = repository.getPersonalTrainers(GymLocation.CLONTARF)

        // Then
        val trainer = result.getOrNull()?.first()
        assertNotNull(trainer)
        assertEquals(2, trainer.socials.size)
        assertEquals("@sarahmurphy_pt", trainer.socials["instagram"])
        assertEquals("@sarahfit", trainer.socials["twitter"])
    }

    @Test
    fun `getPersonalTrainerById preserves all fields`() = runTest {
        // Given
        fakeRemoteDataSource.findPersonalTrainerByIdResponse = PersonalTrainerDtos.emma

        // When
        val result = repository.getPersonalTrainerById(TrainerIds.trainer3)

        // Then
        val trainer = result.getOrNull()
        assertNotNull(trainer)
        assertEquals(TrainerIds.trainer3, trainer.id)
        assertEquals("Emma", trainer.firstName)
        assertEquals("Walsh", trainer.lastName)
        assertTrue(trainer.bio.contains("Yoga instructor"))
        assertEquals(3, trainer.qualifications.size)
        assertEquals(2, trainer.socials.size)
    }
}
