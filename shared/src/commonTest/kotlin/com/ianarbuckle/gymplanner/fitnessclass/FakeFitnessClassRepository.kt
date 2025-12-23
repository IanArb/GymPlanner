package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass

/**
 * Fake implementation of FitnessClassRepository for testing
 */
class FakeFitnessClassRepository(
    private val remoteDataSource: FitnessClassRemoteDataSource
) {

    suspend fun getFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>> {
        return try {
            val classes = remoteDataSource.fitnessClasses(dayOfWeek)
            val fitnessClasses = classes.map { it.transformToFitnessClass() }
            Result.success(fitnessClasses)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}

