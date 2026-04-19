package com.ianarbuckle.gymplanner.facilities.dto

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
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

enum class Location(val displayName: String) {
    MAIN_GYM_FLOOR("Main Gym Floor"),
    BLUE_GYM_FLOOR("Blue Gym Floor"),
    FREE_WEIGHTS_AREA("Free Weights Area"),
    BOX_GYM_FLOOR("Box Gym Floor"),
}

enum class FaultType(val displayName: String) {
    MECHANICAL("Mechanical"),
    ELECTRICAL("Electrical"),
    SOFTWARE("Software"),
    OTHER("Other"),
}
