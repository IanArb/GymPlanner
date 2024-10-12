package com.ianarbuckle.gymplanner.data.fitnessclass

import com.ianarbuckle.gymplanner.mapper.FitnessClassUiModelMapper.transformToFitnessClass
import com.ianarbuckle.gymplanner.model.FitnessClass
import kotlinx.coroutines.flow.Flow

class FitnessClassRepository(
    private val localDataSource: FitnessClassLocalDataSource,
    private val remoteDataSource: FitnessClassRemoteDataSource
) {

    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>> {
        return try {
            val classes = remoteDataSource.fitnessClasses(dayOfWeek)
            classes.map {
                localDataSource.saveFitnessClasses(it.transformToFitnessClass())
            }
            Result.success(
                classes.map {
                    it.transformToFitnessClass()
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun fetchFitnessClassFromLocalStorage(dayOfWeek: String): Flow<List<FitnessClass>> = localDataSource.findAllClients(dayOfWeek)
}