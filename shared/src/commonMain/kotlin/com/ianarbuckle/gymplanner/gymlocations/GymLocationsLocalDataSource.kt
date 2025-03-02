package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocationsMapper.transformToGymLocations
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocationsMapper.transformToGymLocationsRealm
import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GymLocationsLocalDataSource(private val realm: Realm) {

    suspend fun saveGymLocation(gymLocations: GymLocations) {
        realm.write {
            this.copyToRealm(gymLocations.transformToGymLocationsRealm())
        }
    }

    fun gymLocations(): Flow<List<GymLocations>> {
        return realm.query<GymLocationsRealmDto>().asFlow()
            .map {
                when (it) {
                    is InitialResults -> {
                        it.transformToGymLocations()
                    }
                    is UpdatedResults -> {
                        it.transformToGymLocations()
                    }
                }
            }
    }
}
