package com.ianarbuckle.gymplanner.fitnessclass

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FitnessClassRepository {
    suspend fun fetchFitnessClasses(dayOfWeek: String): ApiResult<ImmutableList<FitnessClass>>
}

class DefaultFitnessClassRepository :
    FitnessClassRepository,
    KoinComponent {
    private val remoteDataSource: FitnessClassRemoteDataSource by inject()

    override suspend fun fetchFitnessClasses(dayOfWeek: String): ApiResult<ImmutableList<FitnessClass>> = try {
        val classes = remoteDataSource.fitnessClasses(dayOfWeek)
        val fitnessClasses = classes.map { it.transformToFitnessClass() }
        ApiResult.Success(fitnessClasses.toImmutableList())
    } catch (ex: Exception) {
        if (ex is CancellationException) {
            throw ex
        }
        Logger.withTag("FitnessClassRepository").e("Error fetching fitness classes: $ex")
        ApiResult.Failure(ex)
    }
}
