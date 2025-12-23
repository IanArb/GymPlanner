package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

/**
 * Fake implementation for testing PersonalTrainersRepository
 * Implements the PersonalTrainersRemoteDataSource interface
 */
class FakePersonalTrainersRemoteDataSource : PersonalTrainersRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnFetchPersonalTrainers = false
    var shouldThrowExceptionOnFindPersonalTrainerById = false
    var fetchPersonalTrainersException: Exception? = null
    var findPersonalTrainerByIdException: Exception? = null

    // Captured calls for verification
    val fetchPersonalTrainersCalls = mutableListOf<GymLocation>()
    val findPersonalTrainerByIdCalls = mutableListOf<String>()

    // Configurable responses
    var fetchPersonalTrainersResponse: List<PersonalTrainerDto> = PersonalTrainersTestDataProvider.PersonalTrainerLists.allTrainers
    var findPersonalTrainerByIdResponse: PersonalTrainerDto = PersonalTrainersTestDataProvider.PersonalTrainerDtos.sarah

    override suspend fun fetchPersonalTrainers(gymLocation: GymLocation): List<PersonalTrainerDto> {
        fetchPersonalTrainersCalls.add(gymLocation)

        if (shouldThrowExceptionOnFetchPersonalTrainers) {
            throw fetchPersonalTrainersException ?: RuntimeException("Fetch personal trainers failed")
        }

        return fetchPersonalTrainersResponse
    }

    override suspend fun findPersonalTrainerById(id: String): PersonalTrainerDto {
        findPersonalTrainerByIdCalls.add(id)

        if (shouldThrowExceptionOnFindPersonalTrainerById) {
            throw findPersonalTrainerByIdException ?: RuntimeException("Find personal trainer failed")
        }

        return findPersonalTrainerByIdResponse
    }

    fun reset() {
        shouldThrowExceptionOnFetchPersonalTrainers = false
        shouldThrowExceptionOnFindPersonalTrainerById = false
        fetchPersonalTrainersException = null
        findPersonalTrainerByIdException = null
        fetchPersonalTrainersCalls.clear()
        findPersonalTrainerByIdCalls.clear()

        fetchPersonalTrainersResponse = PersonalTrainersTestDataProvider.PersonalTrainerLists.allTrainers
        findPersonalTrainerByIdResponse = PersonalTrainersTestDataProvider.PersonalTrainerDtos.sarah
    }
}

