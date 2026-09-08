package com.ianarbuckle.gymplanner.android.dashboard.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.common.ApiResult
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList

class FakeFitnessClassRepository : FitnessClassRepository {
    var shouldReturnError = false

    override suspend fun fetchFitnessClasses(dayOfWeek: String): ApiResult<ImmutableList<FitnessClass>> = if (shouldReturnError) {
        mockFitnessClassFailure()
    } else {
        mockFitnessClassSuccess()
    }

    private fun mockFitnessClassSuccess(): ApiResult<ImmutableList<FitnessClass>> = ApiResult.Success(DataProvider.fitnessClasses())

    private fun mockFitnessClassFailure(): ApiResult<ImmutableList<FitnessClass>> = ApiResult.Failure(Exception("Error"))
}
