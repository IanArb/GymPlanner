package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.personaltrainers.domain.PersonalTrainerMapper.toPersonalTrainer


class PersonalTrainersRepository(
    private val remoteDataSource: PersonalTrainersRemoteDataSource,
    private val localDataSource: PersonalTrainersLocalDataSource) {

    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<List<PersonalTrainer>> {
        try {
            val personalTrainers = remoteDataSource.fetchPersonalTrainers(gymLocation)
            val trainers = personalTrainers.map { trainer ->
                trainer.toPersonalTrainer()
            }.onEach { trainer ->
                localDataSource.savePersonalTrainer(trainer)
            }
            return Result.success(trainers)
        } catch (ex: Exception) {
            return Result.failure(ex)
        }
    }

}