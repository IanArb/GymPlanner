package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.personaltrainers.domain.PersonalTrainerMapper.toPersonalTrainer
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList


class PersonalTrainersRepository(
    private val remoteDataSource: PersonalTrainersRemoteDataSource,
    private val localDataSource: PersonalTrainersLocalDataSource) {

    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<ImmutableList<PersonalTrainer>> {
        try {
            val personalTrainers = remoteDataSource.fetchPersonalTrainers(gymLocation)
            val trainers = personalTrainers.map { trainer ->
                trainer.toPersonalTrainer()
            }.onEach { trainer ->
                localDataSource.savePersonalTrainer(trainer)
            }
            return Result.success(trainers.toImmutableList())
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
    }

}