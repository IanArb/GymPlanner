package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus

class FakeFacilitiesRepository(private val remoteDataSource: FacilitiesRemoteDataSource) :
    FacilitiesRepository {

    override suspend fun getFacilitiesStatus(gymLocation: String): List<FacilityStatus> {
        return remoteDataSource.findMachinesByGymLocation(gymLocation).map { it.toFacilityStatus() }
    }
}
