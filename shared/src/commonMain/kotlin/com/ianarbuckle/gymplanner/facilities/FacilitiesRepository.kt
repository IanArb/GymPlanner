package com.ianarbuckle.gymplanner.facilities

import co.touchlab.kermit.Logger
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
                remoteDataSource.findMachinesByGymLocation(gymLocation.name).map {
                    facilityStatus ->
                    facilityStatus.toFacilityStatus()
                }
            }
            .onFailure {
                Logger.withTag(TAG).e("Error fetching facilities status: $it")
                if (it is CancellationException) {
                    Logger.withTag(TAG).e("Operation cancelled: $it")
                    throw it
                }
            }
    }

    companion object {
        private const val TAG = "FacilitiesRepository"
    }
}
