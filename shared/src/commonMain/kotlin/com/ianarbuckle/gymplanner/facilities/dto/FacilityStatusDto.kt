package com.ianarbuckle.gymplanner.facilities.dto

import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.serialization.Serializable

@Serializable
data class FacilityStatusDto(
    val id: String,
    val machineName: String,
    val machineNumber: Int,
    val gymLocation: GymLocation,
    val location: Location,
    val faultType: FaultType,
    val status: MachineStatus,
) {
    fun toFacilityStatus(): FacilityStatus =
        FacilityStatus(
            id = this.id,
            machineName = this.machineName,
            machineNumber = this.machineNumber,
            gymLocation = this.gymLocation,
            location = this.location,
            faultType = this.faultType,
            status = this.status,
        )
}

enum class MachineStatus {
    OPERATIONAL,
    OUT_OF_ORDER,
    UNDER_MAINTENANCE,
}

enum class Location {
    MAIN_GYM_FLOOR,
    BLUE_GYM_FLOOR,
    FREE_WEIGHTS_AREA,
    BOX_GYM_FLOOR,
}

enum class FaultType {
    MECHANICAL,
    ELECTRICAL,
    SOFTWARE,
    OTHER,
}
