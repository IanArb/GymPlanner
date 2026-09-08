package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass

/** Fake implementation of FitnessClassRepository for testing */
class FakeFitnessClassRepository(
    private val remoteDataSource: FitnessClassRemoteDataSource,
) {
    suspend fun getFitnessClasses(dayOfWeek: String): ApiResult<List<FitnessClass>> = try {
        val classes = remoteDataSource.fitnessClasses(dayOfWeek)
        val fitnessClasses = classes.map { it.transformToFitnessClass() }
        ApiResult.Success(fitnessClasses)
    } catch (ex: Exception) {
        ApiResult.Failure(ex)
    }
}
