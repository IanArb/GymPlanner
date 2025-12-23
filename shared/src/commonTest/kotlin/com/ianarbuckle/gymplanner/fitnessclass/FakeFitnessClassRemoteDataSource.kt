package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.fitnessclass.dto.FitnessClassDto

/**
 * Fake implementation for testing FitnessClassRepository Implements the
 * FitnessClassRemoteDataSource interface
 */
class FakeFitnessClassRemoteDataSource : FitnessClassRemoteDataSource {

    // Control flags for test scenarios
    var shouldThrowExceptionOnFitnessClasses = false
    var fitnessClassesException: Exception? = null

    // Captured calls for verification
    val fitnessClassesCalls = mutableListOf<String>()

    // Configurable responses
    var fitnessClassesResponse: List<FitnessClassDto> =
        FitnessClassTestDataProvider.FitnessClassLists.mondayClasses

    override suspend fun fitnessClasses(dayOfWeek: String): List<FitnessClassDto> {
        fitnessClassesCalls.add(dayOfWeek)

        if (shouldThrowExceptionOnFitnessClasses) {
            throw fitnessClassesException ?: RuntimeException("Fetch fitness classes failed")
        }

        return fitnessClassesResponse
    }

    fun reset() {
        shouldThrowExceptionOnFitnessClasses = false
        fitnessClassesException = null
        fitnessClassesCalls.clear()

        fitnessClassesResponse = FitnessClassTestDataProvider.FitnessClassLists.mondayClasses
    }
}
