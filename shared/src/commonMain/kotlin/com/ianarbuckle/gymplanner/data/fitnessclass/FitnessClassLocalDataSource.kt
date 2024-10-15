package com.ianarbuckle.gymplanner.data.fitnessclass

import com.ianarbuckle.gymplanner.mapper.FitnessClassMapper.transformClientDto
import com.ianarbuckle.gymplanner.mapper.FitnessClassMapper.transformToFitnessRealm
import com.ianarbuckle.gymplanner.model.FitnessClass
import com.ianarbuckle.gymplanner.realm.FitnessClassRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FitnessClassLocalDataSource(
    private val realm: Realm,
) {

    suspend fun saveFitnessClasses(fitnessClass: FitnessClass) {
        realm.write {
            this.copyToRealm(fitnessClass.transformToFitnessRealm())
        }
    }

    fun findAllClients(dayOfWeek: String): Flow<List<FitnessClass>> {
        return realm.query<FitnessClassRealmDto>("dayOfWeek = $0", dayOfWeek).asFlow()
            .map {
                when (it) {
                    is InitialResults -> {
                        it.transformClientDto()
                    }
                    is UpdatedResults -> {
                        it.transformClientDto()
                    }
                }
            }
    }
}