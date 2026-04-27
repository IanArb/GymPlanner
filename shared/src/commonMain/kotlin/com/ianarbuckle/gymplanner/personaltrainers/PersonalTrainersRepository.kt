package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.common.PersonalTrainer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PersonalTrainersRepository {
    suspend fun fetchPersonalTrainers(
        gymLocation: GymLocation
    ): Result<ImmutableList<PersonalTrainer>>

    suspend fun findPersonalTrainerById(id: String): Result<PersonalTrainer>

    suspend fun fetchTrainerSchedules(date: String): Result<ImmutableList<PersonalTrainer>>
}

class DefaultPersonalTrainersRepository : PersonalTrainersRepository, KoinComponent {

    private val remoteDataSource: PersonalTrainersRemoteDataSource by inject()

    override suspend fun fetchPersonalTrainers(
        gymLocation: GymLocation
    ): Result<ImmutableList<PersonalTrainer>> {
        try {
            val personalTrainers = remoteDataSource.fetchPersonalTrainers(gymLocation)
            val trainers = personalTrainers.map { trainer -> trainer.toPersonalTrainer() }
            return Result.success(trainers.toImmutableList())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }

    override suspend fun findPersonalTrainerById(id: String): Result<PersonalTrainer> {
        try {
            val personalTrainer = remoteDataSource.findPersonalTrainerById(id)
            return Result.success(personalTrainer.toPersonalTrainer())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }

    override suspend fun fetchTrainerSchedules(
        date: String
    ): Result<ImmutableList<PersonalTrainer>> {
        try {
            val response = remoteDataSource.fetchTrainerSchedules(date)
            val trainers = response.map { it.toPersonalTrainer() }.toImmutableList()
            return Result.success(trainers)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }
}
