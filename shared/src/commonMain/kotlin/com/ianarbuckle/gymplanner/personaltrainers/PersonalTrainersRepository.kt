package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.personaltrainers.domain.PersonalTrainerMapper.toPersonalTrainer
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface PersonalTrainersRepository {
    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<ImmutableList<PersonalTrainer>>
}

class DefaultPersonalTrainersRepository : PersonalTrainersRepository, KoinComponent {

    private val remoteDataSource: PersonalTrainersRemoteDataSource by inject()
    private val localDataSource: PersonalTrainersLocalDataSource by inject()

    override suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<ImmutableList<PersonalTrainer>> {
        try {
            val personalTrainers = remoteDataSource.fetchPersonalTrainers(gymLocation)
            val trainers = personalTrainers.map { trainer ->
                trainer.toPersonalTrainer()
            }.onEach { trainer ->
                localDataSource.savePersonalTrainer(trainer)
            }
            return Result.success(trainers.toImmutableList())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }
}
