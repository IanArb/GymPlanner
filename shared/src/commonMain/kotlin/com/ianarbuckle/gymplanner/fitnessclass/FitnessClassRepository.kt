package com.ianarbuckle.gymplanner.fitnessclass

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClassUiModelMapper.transformToFitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FitnessClassRepository {
    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<ImmutableList<FitnessClass>>
    fun fetchFitnessClassFromLocalStorage(dayOfWeek: String): Flow<List<FitnessClass>>
}

class DefaultFitnessClassRepository : FitnessClassRepository, KoinComponent {

    private val localDataSource: FitnessClassLocalDataSource by inject()
    private val remoteDataSource: FitnessClassRemoteDataSource by inject()

    override suspend fun fetchFitnessClasses(dayOfWeek: String): Result<ImmutableList<FitnessClass>> {
        return try {
            val classes = remoteDataSource.fitnessClasses(dayOfWeek)
            classes.map {
                localDataSource.saveFitnessClasses(it.transformToFitnessClass())
            }
            val fitnessClasses = classes.map {
                it.transformToFitnessClass()
            }
            Result.success(fitnessClasses.toImmutableList())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("FitnessClassRepository").e("Error fetching fitness classes: $ex")
            return Result.failure(ex)
        }
    }

    override fun fetchFitnessClassFromLocalStorage(dayOfWeek: String): Flow<List<FitnessClass>> = localDataSource.findAllClients(dayOfWeek)
}
