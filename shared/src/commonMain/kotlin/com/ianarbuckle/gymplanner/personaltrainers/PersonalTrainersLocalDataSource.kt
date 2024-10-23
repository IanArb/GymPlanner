package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.personaltrainers.domain.PersonalTrainerMapper
import com.ianarbuckle.gymplanner.personaltrainers.domain.PersonalTrainerMapper.transformToPersonalTrainers
import com.ianarbuckle.gymplanner.personaltrainers.dto.PersonalTrainersRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PersonalTrainersLocalDataSource(private val realm: Realm) {

    suspend fun savePersonalTrainer(personalTrainer: PersonalTrainer) {
        realm.write {
            val personalTrainerRealmDto = PersonalTrainerMapper.mapPersonalTrainers(personalTrainer)
            this.copyToRealm(personalTrainerRealmDto)
        }
    }

    fun findAllPersonalTrainers(gymLocation: GymLocation): Flow<List<PersonalTrainer>> {
        return realm.query<PersonalTrainersRealmDto>("gymLocation = $0", gymLocation).asFlow()
            .map {
                when (it) {
                    is InitialResults -> {
                        it.transformToPersonalTrainers()
                    }
                    is UpdatedResults -> {
                        it.transformToPersonalTrainers()
                    }
                }
            }
    }
}