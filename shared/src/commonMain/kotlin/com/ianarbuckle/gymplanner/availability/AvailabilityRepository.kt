package com.ianarbuckle.gymplanner.availability

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface AvailabilityRepository {
    suspend fun getAvailability(personalTrainerId: String, month: String): Result<Availability>

    suspend fun checkAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<CheckAvailability>
}

class DefaultAvailabilityRepository : AvailabilityRepository, KoinComponent {

    private val remoteDataSource: AvailabilityRemoteDataSource by inject()

    override suspend fun getAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<Availability> {
        return try {
            val availabilityDto =
                remoteDataSource.fetchAvailability(
                    personalTrainerId = personalTrainerId,
                    month = month,
                )
            Result.success(availabilityDto.toAvailability())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("AvailabilityRepository").e("Error fetching availability: $ex")
            return Result.failure(ex)
        }
    }

    override suspend fun checkAvailability(
        personalTrainerId: String,
        month: String,
    ): Result<CheckAvailability> {
        return try {
            val checkAvailabilityDto =
                remoteDataSource.checkAvailability(
                    personalTrainerId = personalTrainerId,
                    month = month,
                )
            Result.success(checkAvailabilityDto.toCheckAvailability())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.e("Error checking availability: $ex")
            return Result.failure(ex)
        }
    }
}
