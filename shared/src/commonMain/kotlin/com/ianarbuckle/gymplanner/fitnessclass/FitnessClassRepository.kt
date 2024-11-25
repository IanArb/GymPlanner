package com.ianarbuckle.gymplanner.fitnessclass

import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClassUiModelMapper.transformToFitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow

class FitnessClassRepository(
    private val localDataSource: FitnessClassLocalDataSource,
    private val remoteDataSource: FitnessClassRemoteDataSource,
) {

    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<ImmutableList<FitnessClass>> {
        return try {
            val classes = remoteDataSource.fitnessClasses(dayOfWeek)
            classes.map {
                localDataSource.saveFitnessClasses(it.transformToFitnessClass())
            }
            val fitnessClasses = classes.map {
                it.transformToFitnessClass()
            }
            Result.success(fitnessClasses.toImmutableList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun fetchFitnessClassFromLocalStorage(dayOfWeek: String): Flow<List<FitnessClass>> = localDataSource.findAllClients(dayOfWeek)
}