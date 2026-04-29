package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.common.GymLocation

/**
 * Fake implementation for testing PersonalTrainersRepository Implements the
 * PersonalTrainersRemoteDataSource interface
 */
class FakePersonalTrainersRemoteDataSource : PersonalTrainersRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnFetchPersonalTrainers = false
    var shouldThrowExceptionOnFindPersonalTrainerById = false
    var shouldThrowExceptionOnFetchTrainerSchedules = false
    var fetchPersonalTrainersException: Exception? = null
    var findPersonalTrainerByIdException: Exception? = null
    var fetchTrainerSchedulesException: Exception? = null

    // Captured calls for verification
    val fetchPersonalTrainersCalls = mutableListOf<GymLocation>()
    val findPersonalTrainerByIdCalls = mutableListOf<String>()
    val fetchTrainerSchedulesCalls = mutableListOf<Pair<String, GymLocation>>()

    // Configurable responses
    var fetchPersonalTrainersResponse: List<PersonalTrainerDto> =
        PersonalTrainersTestDataProvider.PersonalTrainerLists.allTrainers
    var findPersonalTrainerByIdResponse: PersonalTrainerDto =
        PersonalTrainersTestDataProvider.PersonalTrainerDtos.sarah
    var fetchTrainerSchedulesResponse: List<PersonalTrainerDto> =
        PersonalTrainersTestDataProvider.PersonalTrainerLists.allTrainers

    override suspend fun fetchPersonalTrainers(gymLocation: GymLocation): List<PersonalTrainerDto> {
        fetchPersonalTrainersCalls.add(gymLocation)

        if (shouldThrowExceptionOnFetchPersonalTrainers) {
            throw fetchPersonalTrainersException
                ?: RuntimeException("Fetch personal trainers failed")
        }

        return fetchPersonalTrainersResponse
    }

    override suspend fun findPersonalTrainerById(id: String): PersonalTrainerDto {
        findPersonalTrainerByIdCalls.add(id)

        if (shouldThrowExceptionOnFindPersonalTrainerById) {
            throw findPersonalTrainerByIdException
                ?: RuntimeException("Find personal trainer failed")
        }

        return findPersonalTrainerByIdResponse
    }

    override suspend fun fetchTrainerSchedules(
        date: String,
        gymLocation: GymLocation,
    ): List<PersonalTrainerDto> {
        fetchTrainerSchedulesCalls.add(date to gymLocation)

        if (shouldThrowExceptionOnFetchTrainerSchedules) {
            throw fetchTrainerSchedulesException
                ?: RuntimeException("Fetch trainer schedules failed")
        }

        return fetchTrainerSchedulesResponse
    }

    fun reset() {
        shouldThrowExceptionOnFetchPersonalTrainers = false
        shouldThrowExceptionOnFindPersonalTrainerById = false
        shouldThrowExceptionOnFetchTrainerSchedules = false
        fetchPersonalTrainersException = null
        findPersonalTrainerByIdException = null
        fetchTrainerSchedulesException = null
        fetchPersonalTrainersCalls.clear()
        findPersonalTrainerByIdCalls.clear()
        fetchTrainerSchedulesCalls.clear()

        fetchPersonalTrainersResponse =
            PersonalTrainersTestDataProvider.PersonalTrainerLists.allTrainers
        findPersonalTrainerByIdResponse = PersonalTrainersTestDataProvider.PersonalTrainerDtos.sarah
        fetchTrainerSchedulesResponse =
            PersonalTrainersTestDataProvider.PersonalTrainerLists.allTrainers
    }
}
