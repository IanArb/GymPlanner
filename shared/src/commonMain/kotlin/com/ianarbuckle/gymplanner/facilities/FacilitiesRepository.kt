package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface FacilitiesRepository {
    suspend fun getFacilitiesStatus(gymLocation: GymLocation): Result<List<FacilityStatus>>
}

class DefaultFacilitiesRepository : FacilitiesRepository, KoinComponent {

    private val remoteDataSource: FacilitiesRemoteDataSource by inject()

    override suspend fun getFacilitiesStatus(
        gymLocation: GymLocation
    ): Result<List<FacilityStatus>> {
        return runCatching {
                remoteDataSource.findMachinesByGymLocation(gymLocation.toString()).map {
                    facilityStatus ->
                    facilityStatus.toFacilityStatus()
                }
            }
            .onFailure {
                if (it is CancellationException) {
                    throw it
                }
            }
    }
}
