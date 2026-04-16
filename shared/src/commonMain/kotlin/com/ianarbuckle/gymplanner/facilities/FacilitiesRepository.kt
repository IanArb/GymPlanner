package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FacilitiesRepository {
    suspend fun getFacilitiesStatus(gymLocation: String): Result<List<FacilityStatus>>
}

class DefaultFacilitiesRepository : FacilitiesRepository, KoinComponent {

    private val remoteDataSource: FacilitiesRemoteDataSource by inject()

    override suspend fun getFacilitiesStatus(gymLocation: String): Result<List<FacilityStatus>> {
        return runCatching {
            remoteDataSource.findMachinesByGymLocation(gymLocation).map { facilityStatus ->
                facilityStatus.toFacilityStatus()
            }
        }
    }
}
