package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

class FakeFacilitiesRepository(private val remoteDataSource: FacilitiesRemoteDataSource) :
    FacilitiesRepository {

    override suspend fun getFacilitiesStatus(
        gymLocation: GymLocation
    ): Result<List<FacilityStatus>> {
        return runCatching {
            remoteDataSource.findMachinesByGymLocation(gymLocation.toString()).map {
                it.toFacilityStatus()
            }
        }
    }
}
