package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.dto.FacilityStatusDto

class FakeFacilitiesRemoteDataSource : FacilitiesRemoteDataSource {

    var shouldThrowException = false
    var exception: Exception? = null

    val findByLocationCalls = mutableListOf<String>()

    var facilitiesResponse: List<FacilityStatusDto> =
        FacilitiesTestDataProvider.FacilityLists.multipleStatuses

    override suspend fun findMachinesByGymLocation(gymLocation: String): List<FacilityStatusDto> {
        findByLocationCalls.add(gymLocation)

        if (shouldThrowException) {
            throw exception ?: RuntimeException("Fetch facilities failed")
        }

        return facilitiesResponse
    }

    fun reset() {
        shouldThrowException = false
        exception = null
        findByLocationCalls.clear()
        facilitiesResponse = FacilitiesTestDataProvider.FacilityLists.multipleStatuses
    }
}
