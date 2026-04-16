package com.ianarbuckle.gymplanner.facilities.domain

import com.ianarbuckle.gymplanner.facilities.dto.FaultType
import com.ianarbuckle.gymplanner.facilities.dto.Location
import com.ianarbuckle.gymplanner.facilities.dto.MachineStatus
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

data class FacilityStatus(
    val id: String,
    val machineName: String,
    val machineNumber: Int,
    val gymLocation: GymLocation,
    val location: Location,
    val faultType: FaultType,
    val status: MachineStatus,
)
