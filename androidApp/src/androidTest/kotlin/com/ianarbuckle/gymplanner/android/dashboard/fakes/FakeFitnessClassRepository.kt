package com.ianarbuckle.gymplanner.android.dashboard.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import kotlinx.collections.immutable.ImmutableList

class FakeFitnessClassRepository : FitnessClassRepository {

    var shouldReturnError = false

    override suspend fun fetchFitnessClasses(
        dayOfWeek: String
    ): Result<ImmutableList<FitnessClass>> {
        return if (shouldReturnError) {
            mockFitnessClassFailure()
        } else {
            mockFitnessClassSuccess()
        }
    }

    private fun mockFitnessClassSuccess(): Result<ImmutableList<FitnessClass>> {
        return Result.success(DataProvider.fitnessClasses())
    }

    private fun mockFitnessClassFailure(): Result<ImmutableList<FitnessClass>> {
        return Result.failure(Exception("Error"))
    }
}
