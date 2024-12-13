package com.ianarbuckle.gymplanner.availability

import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.AvailabilityMapper.toAvailability
import com.ianarbuckle.gymplanner.availability.domain.AvailabilityMapper.toCheckAvailability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException
import okio.IOException

class AvailabilityRepository(private val remoteDataSource: AvailabilityRemoteDataSource) {

    suspend fun getAvailability(personalTrainerId: String, month: String): Result<Availability> {
        return try {
            val availabilityDto = remoteDataSource.fetchAvailability(
                personalTrainerId = personalTrainerId,
                month = month,
            )
            Result.success(availabilityDto.toAvailability())
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
        catch (ex: IOException) {
            return Result.failure(ex)
        }
        catch (ex: SerializationException) {
            return Result.failure(ex)
        }
        catch (ex: JsonConvertException) {
            return Result.failure(ex)
        }
        catch (ex: NoTransformationFoundException) {
            return Result.failure(ex)
        }
    }

    suspend fun checkAvailability(personalTrainerId: String, month: String): Result<CheckAvailability> {
        return try {
            val checkAvailabilityDto = remoteDataSource.checkAvailability(
                personalTrainerId = personalTrainerId,
                month = month,
            )
            Result.success(checkAvailabilityDto.toCheckAvailability())
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
        catch (ex: IOException) {
            return Result.failure(ex)
        }
        catch (ex: SerializationException) {
            return Result.failure(ex)
        }
        catch (ex: JsonConvertException) {
            return Result.failure(ex)
        }
        catch (ex: NoTransformationFoundException) {
            return Result.failure(ex)
        }
    }

}