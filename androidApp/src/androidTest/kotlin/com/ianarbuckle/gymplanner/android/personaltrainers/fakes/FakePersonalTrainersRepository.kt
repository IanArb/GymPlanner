package com.ianarbuckle.gymplanner.android.personaltrainers.fakes

import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.ImmutableList

class FakePersonalTrainersRepository : PersonalTrainersRepository {

  override suspend fun fetchPersonalTrainers(
    gymLocation: GymLocation
  ): Result<ImmutableList<PersonalTrainer>> {
    return mockPersonalTrainers()
  }

  private fun mockPersonalTrainers(): Result<ImmutableList<PersonalTrainer>> {
    return Result.success(DataProvider.personalTrainers())
  }

  override suspend fun findPersonalTrainerById(id: String): Result<PersonalTrainer> {
    return Result.success(DataProvider.personalTrainers().first())
  }
}
