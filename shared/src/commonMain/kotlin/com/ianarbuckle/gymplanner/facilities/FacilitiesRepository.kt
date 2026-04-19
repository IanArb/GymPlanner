package com.ianarbuckle.gymplanner.facilities

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.facilities.dto.MachineStatus
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
                remoteDataSource
                    .findMachinesByGymLocation(gymLocation.name)
                    .map { it.toFacilityStatus() }
                    .sortedBy { statusPriority(it.status) }
            }
            .onFailure {
                if (it is CancellationException) {
                    Logger.withTag(TAG).e("Operation cancelled: $it")
                    throw it
                }
                Logger.withTag(TAG).e("Error fetching facilities status: $it")
            }
    }

    private fun statusPriority(status: MachineStatus): Int =
        when (status) {
            MachineStatus.OUT_OF_ORDER -> 0
            MachineStatus.UNDER_MAINTENANCE -> 1
            MachineStatus.OPERATIONAL -> 2
        }

    companion object {
        private const val TAG = "FacilitiesRepository"
    }
}
