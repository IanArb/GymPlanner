package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.facilities.dto.MachineStatus

class FakeFacilitiesRepository(private val remoteDataSource: FacilitiesRemoteDataSource) :
    FacilitiesRepository {

    override suspend fun getFacilitiesStatus(
        gymLocation: GymLocation
    ): Result<List<FacilityStatus>> {
        return runCatching {
            remoteDataSource
                .findMachinesByGymLocation(gymLocation.toString())
                .map { it.toFacilityStatus() }
                .sortedBy { statusPriority(it.status) }
        }
    }

    private fun statusPriority(status: MachineStatus): Int =
        when (status) {
            MachineStatus.OUT_OF_ORDER -> 0
            MachineStatus.UNDER_MAINTENANCE -> 1
            MachineStatus.OPERATIONAL -> 2
        }
}
