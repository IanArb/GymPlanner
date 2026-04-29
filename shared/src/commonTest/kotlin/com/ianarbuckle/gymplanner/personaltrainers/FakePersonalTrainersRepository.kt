package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.common.PersonalTrainer

/** Fake implementation of PersonalTrainersRepository for testing */
class FakePersonalTrainersRepository(
    private val remoteDataSource: PersonalTrainersRemoteDataSource
) {

    suspend fun getPersonalTrainers(gymLocation: GymLocation): Result<List<PersonalTrainer>> {
        return try {
            val trainers = remoteDataSource.fetchPersonalTrainers(gymLocation)
            val personalTrainers = trainers.map { it.toPersonalTrainer() }
            Result.success(personalTrainers)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getPersonalTrainerById(id: String): Result<PersonalTrainer> {
        return try {
            val trainer = remoteDataSource.findPersonalTrainerById(id)
            Result.success(trainer.toPersonalTrainer())
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getTrainerSchedules(
        date: String,
        gymLocation: GymLocation,
    ): Result<List<PersonalTrainer>> {
        return try {
            val trainers = remoteDataSource.fetchTrainerSchedules(date, gymLocation)
            Result.success(trainers.map { it.toPersonalTrainer() })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
