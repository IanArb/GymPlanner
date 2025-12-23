package com.ianarbuckle.gymplanner.availability

import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability

/** Fake implementation of AvailabilityRepository for testing */
class FakeAvailabilityRepository(private val remoteDataSource: AvailabilityRemoteDataSource) :
    AvailabilityRepository {

    override suspend fun getAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<Availability> {
        return try {
            val result = remoteDataSource.fetchAvailability(personalTrainerId, month)
            Result.success(result.toAvailability())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    override suspend fun checkAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<CheckAvailability> {
        return try {
            val result = remoteDataSource.checkAvailability(personalTrainerId, month)
            Result.success(result.toCheckAvailability())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
