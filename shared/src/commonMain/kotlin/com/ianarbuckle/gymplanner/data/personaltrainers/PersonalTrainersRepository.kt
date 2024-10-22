package com.ianarbuckle.gymplanner.data.personaltrainers

import com.ianarbuckle.gymplanner.mapper.PersonalTrainerMapper.toPersonalTrainer
import com.ianarbuckle.gymplanner.model.GymLocation
import com.ianarbuckle.gymplanner.model.PersonalTrainer
import com.ianarbuckle.gymplanner.model.PersonalTrainerList

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