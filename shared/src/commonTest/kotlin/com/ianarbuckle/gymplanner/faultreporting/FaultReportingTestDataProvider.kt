package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.dto.FaultReportDto

/**
 * Provides test data for FaultReportingRepository tests
 */
object FaultReportingTestDataProvider {

    // ========== Machine Numbers ==========

    object MachineNumbers {
        const val treadmill1 = 101
        const val bike2 = 202
        const val rower3 = 303
        const val elliptical4 = 404
        const val invalidMachine = -1
        const val zeroMachine = 0
    }

    // ========== Dates ==========

    object Dates {
        const val today = "2025-12-23"
        const val yesterday = "2025-12-22"
        const val lastWeek = "2025-12-16"
        const val invalidDate = "invalid-date"
    }

    // ========== Photo URIs ==========

    object PhotoUris {
        const val photo1 = "file:///storage/photos/fault1.jpg"
        const val photo2 = "file:///storage/photos/fault2.jpg"
        const val photo3 = "file:///storage/photos/fault3.png"
        const val emptyUri = ""
        const val httpUri = "https://example.com/photo.jpg"
    }

    // ========== Descriptions ==========

    object Descriptions {
        const val treadmillBelt = "Treadmill belt is slipping and making grinding noise"
        const val bikeResistance = "Bike resistance knob is stuck and won't adjust"
        const val rowerDisplay = "Rower display screen is flickering and showing errors"
        const val ellipticalPedal = "Elliptical left pedal is loose and wobbling"
        const val emptyDescription = ""
        const val longDescription = "This is a very long and detailed description of a fault that includes multiple issues with the machine including mechanical problems, electrical issues, and safety concerns that need immediate attention from the maintenance team."
    }

    // ========== Fault Report DTOs ==========

    object FaultReportDtos {
        val treadmillFault = FaultReportDto(
            id = "fault-001",
            machineNumber = MachineNumbers.treadmill1,
            description = Descriptions.treadmillBelt,
            photoUri = PhotoUris.photo1,
            date = Dates.today
        )

        val bikeFault = FaultReportDto(
            id = "fault-002",
            machineNumber = MachineNumbers.bike2,
            description = Descriptions.bikeResistance,
            photoUri = PhotoUris.photo2,
            date = Dates.yesterday
        )

        val rowerFault = FaultReportDto(
            id = "fault-003",
            machineNumber = MachineNumbers.rower3,
            description = Descriptions.rowerDisplay,
            photoUri = PhotoUris.photo3,
            date = Dates.lastWeek
        )

        val savedReport = FaultReportDto(
            id = "fault-004",
            machineNumber = MachineNumbers.elliptical4,
            description = Descriptions.ellipticalPedal,
            photoUri = PhotoUris.photo1,
            date = Dates.today
        )
    }

    // ========== Fault Reports (Domain) ==========

    object FaultReports {
        val treadmillFault = FaultReport(
            description = Descriptions.treadmillBelt,
            photoUri = PhotoUris.photo1,
            machineNumber = MachineNumbers.treadmill1,
            date = Dates.today
        )

        val bikeFault = FaultReport(
            description = Descriptions.bikeResistance,
            photoUri = PhotoUris.photo2,
            machineNumber = MachineNumbers.bike2,
            date = Dates.yesterday
        )

        val rowerFault = FaultReport(
            description = Descriptions.rowerDisplay,
            photoUri = PhotoUris.photo3,
            machineNumber = MachineNumbers.rower3,
            date = Dates.lastWeek
        )

        val ellipticalFault = FaultReport(
            description = Descriptions.ellipticalPedal,
            photoUri = PhotoUris.photo1,
            machineNumber = MachineNumbers.elliptical4,
            date = Dates.today
        )

        val newReport = FaultReport(
            description = "New machine fault to be saved",
            photoUri = PhotoUris.photo2,
            machineNumber = 505,
            date = Dates.today
        )

        val emptyDescriptionReport = FaultReport(
            description = Descriptions.emptyDescription,
            photoUri = PhotoUris.photo1,
            machineNumber = MachineNumbers.treadmill1,
            date = Dates.today
        )

        val longDescriptionReport = FaultReport(
            description = Descriptions.longDescription,
            photoUri = PhotoUris.photo1,
            machineNumber = MachineNumbers.bike2,
            date = Dates.today
        )

        val invalidMachineReport = FaultReport(
            description = Descriptions.treadmillBelt,
            photoUri = PhotoUris.photo1,
            machineNumber = MachineNumbers.invalidMachine,
            date = Dates.today
        )
    }

    // ========== Report Lists ==========

    object ReportLists {
        val multipleReports = listOf(
            FaultReportDtos.treadmillFault,
            FaultReportDtos.bikeFault,
            FaultReportDtos.rowerFault
        )

        val singleReport = listOf(FaultReportDtos.treadmillFault)

        val emptyList = emptyList<FaultReportDto>()
    }

    object DomainReportLists {
        val multipleReports = listOf(
            FaultReports.treadmillFault,
            FaultReports.bikeFault,
            FaultReports.rowerFault
        )

        val singleReport = listOf(FaultReports.treadmillFault)

        val emptyList = emptyList<FaultReport>()
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("Fault report not found")
        val validationError = IllegalArgumentException("Invalid fault report data")
        val timeout = Exception("Request timeout")
        val saveFailed = RuntimeException("Failed to save fault report")
    }
}

