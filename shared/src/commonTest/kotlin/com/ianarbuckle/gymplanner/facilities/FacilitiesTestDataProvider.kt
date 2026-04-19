package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.facilities.dto.FacilityStatusDto
import com.ianarbuckle.gymplanner.facilities.dto.FaultType
import com.ianarbuckle.gymplanner.facilities.dto.Location
import com.ianarbuckle.gymplanner.facilities.dto.MachineStatus

object FacilitiesTestDataProvider {

    object GymLocations {
        val clontarf = GymLocation.CLONTARF
        val astonQuay = GymLocation.ASTONQUAY
        val leopardstown = GymLocation.LEOPARDSTOWN
    }

    object MachineNames {
        const val treadmill = "Treadmill"
        const val rowingMachine = "Rowing Machine"
        const val elliptical = "Elliptical"
        const val spinBike = "Spin Bike"
    }

    object MachineNumbers {
        const val treadmill1 = 1
        const val rower2 = 2
        const val elliptical3 = 3
        const val bike4 = 4
    }

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val timeout = Exception("Request timeout")
    }

    object FacilityStatusDtos {
        val treadmillOperational =
            FacilityStatusDto(
                id = "facility-001",
                machineName = MachineNames.treadmill,
                machineNumber = MachineNumbers.treadmill1,
                gymLocation = GymLocation.CLONTARF,
                location = Location.MAIN_GYM_FLOOR,
                faultType = FaultType.OTHER,
                status = MachineStatus.OPERATIONAL,
            )

        val rowerOutOfOrder =
            FacilityStatusDto(
                id = "facility-002",
                machineName = MachineNames.rowingMachine,
                machineNumber = MachineNumbers.rower2,
                gymLocation = GymLocation.CLONTARF,
                location = Location.BLUE_GYM_FLOOR,
                faultType = FaultType.MECHANICAL,
                status = MachineStatus.OUT_OF_ORDER,
            )

        val ellipticalUnderMaintenance =
            FacilityStatusDto(
                id = "facility-003",
                machineName = MachineNames.elliptical,
                machineNumber = MachineNumbers.elliptical3,
                gymLocation = GymLocation.ASTONQUAY,
                location = Location.FREE_WEIGHTS_AREA,
                faultType = FaultType.ELECTRICAL,
                status = MachineStatus.UNDER_MAINTENANCE,
            )

        val spinBikeOperational =
            FacilityStatusDto(
                id = "facility-004",
                machineName = MachineNames.spinBike,
                machineNumber = MachineNumbers.bike4,
                gymLocation = GymLocation.LEOPARDSTOWN,
                location = Location.BOX_GYM_FLOOR,
                faultType = FaultType.SOFTWARE,
                status = MachineStatus.OPERATIONAL,
            )
    }

    object FacilityStatuses {
        val treadmillOperational =
            FacilityStatus(
                id = "facility-001",
                machineName = MachineNames.treadmill,
                machineNumber = MachineNumbers.treadmill1,
                gymLocation = GymLocation.CLONTARF,
                location = Location.MAIN_GYM_FLOOR,
                faultType = FaultType.OTHER,
                status = MachineStatus.OPERATIONAL,
            )

        val rowerOutOfOrder =
            FacilityStatus(
                id = "facility-002",
                machineName = MachineNames.rowingMachine,
                machineNumber = MachineNumbers.rower2,
                gymLocation = GymLocation.CLONTARF,
                location = Location.BLUE_GYM_FLOOR,
                faultType = FaultType.MECHANICAL,
                status = MachineStatus.OUT_OF_ORDER,
            )

        val ellipticalUnderMaintenance =
            FacilityStatus(
                id = "facility-003",
                machineName = MachineNames.elliptical,
                machineNumber = MachineNumbers.elliptical3,
                gymLocation = GymLocation.ASTONQUAY,
                location = Location.FREE_WEIGHTS_AREA,
                faultType = FaultType.ELECTRICAL,
                status = MachineStatus.UNDER_MAINTENANCE,
            )

        val spinBikeOperational =
            FacilityStatus(
                id = "facility-004",
                machineName = MachineNames.spinBike,
                machineNumber = MachineNumbers.bike4,
                gymLocation = GymLocation.LEOPARDSTOWN,
                location = Location.BOX_GYM_FLOOR,
                faultType = FaultType.SOFTWARE,
                status = MachineStatus.OPERATIONAL,
            )
    }

    object FacilityLists {
        val multipleStatuses =
            listOf(
                FacilityStatusDtos.treadmillOperational,
                FacilityStatusDtos.rowerOutOfOrder,
                FacilityStatusDtos.ellipticalUnderMaintenance,
            )

        val singleStatus = listOf(FacilityStatusDtos.treadmillOperational)

        val emptyList = emptyList<FacilityStatusDto>()
    }

    object DomainFacilityLists {
        val multipleStatuses =
            listOf(
                FacilityStatuses.rowerOutOfOrder,
                FacilityStatuses.ellipticalUnderMaintenance,
                FacilityStatuses.treadmillOperational,
            )

        val singleStatus = listOf(FacilityStatuses.treadmillOperational)

        val emptyList = emptyList<FacilityStatus>()
    }
}
