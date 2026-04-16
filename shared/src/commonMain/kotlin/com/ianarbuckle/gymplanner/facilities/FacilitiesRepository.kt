package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FacilitiesRepository {
    suspend fun getFacilitiesStatus(gymLocation: String): List<FacilityStatus>
}

class DefaultFacilitiesRepository() : FacilitiesRepository, KoinComponent {

    private val remoteDataSource: FacilitiesRemoteDataSource by inject()

    override suspend fun getFacilitiesStatus(gymLocation: String): List<FacilityStatus> {
        val facilities =
            remoteDataSource.findMachinesByGymLocation(gymLocation).map { facilityStatus ->
                facilityStatus.toFacilityStatus()
            }
        return facilities
    }
}
