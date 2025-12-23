package com.ianarbuckle.gymplanner.gymlocations

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsDto

/**
 * Provides test data for GymLocationsRepository tests
 */
object GymLocationsTestDataProvider {

    // ========== Gym Location DTOs ==========

    object GymLocationDtos {
        val clontarf = GymLocationsDto(
            id = "gym-001",
            title = "Clontarf",
            subTitle = "Coastal Dublin Location",
            description = "Modern gym facility located on the beautiful Clontarf seafront with stunning bay views. Features state-of-the-art equipment and spacious workout areas.",
            imageUrl = "https://example.com/clontarf.jpg"
        )

        val astonQuay = GymLocationsDto(
            id = "gym-002",
            title = "Aston Quay",
            subTitle = "City Centre Location",
            description = "Prime city centre location on the River Liffey. Perfect for professionals looking for a convenient workout before or after work.",
            imageUrl = "https://example.com/astonquay.jpg"
        )

        val leopardstown = GymLocationsDto(
            id = "gym-003",
            title = "Leopardstown",
            subTitle = "South Dublin Location",
            description = "Large premium facility in South Dublin's business district. Offers extensive cardio and weights areas plus group fitness studio.",
            imageUrl = "https://example.com/leopardstown.jpg"
        )

        val sandymount = GymLocationsDto(
            id = "gym-004",
            title = "Sandymount",
            subTitle = "Seaside Location",
            description = "Boutique gym with a focus on personal training and small group classes. Located minutes from Sandymount Strand.",
            imageUrl = "https://example.com/sandymount.jpg"
        )

        val dunLaoghaire = GymLocationsDto(
            id = "gym-005",
            title = "Dun Laoghaire",
            subTitle = "Harbor Town Location",
            description = "Full-service gym in the heart of Dun Laoghaire with pool, sauna, and recovery facilities. Family-friendly environment.",
            imageUrl = "https://example.com/dunlaoghaire.jpg"
        )

        val westmanstown = GymLocationsDto(
            id = "gym-006",
            title = "Westmanstown",
            subTitle = "West Dublin Location",
            description = "Community-focused gym with excellent parking and accessibility. Offers programs for all ages and fitness levels.",
            imageUrl = "https://example.com/westmanstown.jpg"
        )
    }

    // ========== Gym Locations (Domain) ==========

    object GymLocationsDomain {
        val clontarf = GymLocations(
            title = "Clontarf",
            subTitle = "Coastal Dublin Location",
            description = "Modern gym facility located on the beautiful Clontarf seafront with stunning bay views. Features state-of-the-art equipment and spacious workout areas.",
            imageUrl = "https://example.com/clontarf.jpg"
        )

        val astonQuay = GymLocations(
            title = "Aston Quay",
            subTitle = "City Centre Location",
            description = "Prime city centre location on the River Liffey. Perfect for professionals looking for a convenient workout before or after work.",
            imageUrl = "https://example.com/astonquay.jpg"
        )

        val leopardstown = GymLocations(
            title = "Leopardstown",
            subTitle = "South Dublin Location",
            description = "Large premium facility in South Dublin's business district. Offers extensive cardio and weights areas plus group fitness studio.",
            imageUrl = "https://example.com/leopardstown.jpg"
        )

        val sandymount = GymLocations(
            title = "Sandymount",
            subTitle = "Seaside Location",
            description = "Boutique gym with a focus on personal training and small group classes. Located minutes from Sandymount Strand.",
            imageUrl = "https://example.com/sandymount.jpg"
        )

        val dunLaoghaire = GymLocations(
            title = "Dun Laoghaire",
            subTitle = "Harbor Town Location",
            description = "Full-service gym in the heart of Dun Laoghaire with pool, sauna, and recovery facilities. Family-friendly environment.",
            imageUrl = "https://example.com/dunlaoghaire.jpg"
        )

        val westmanstown = GymLocations(
            title = "Westmanstown",
            subTitle = "West Dublin Location",
            description = "Community-focused gym with excellent parking and accessibility. Offers programs for all ages and fitness levels.",
            imageUrl = "https://example.com/westmanstown.jpg"
        )
    }

    // ========== Gym Location Lists ==========

    object GymLocationLists {
        val allLocations = listOf(
            GymLocationDtos.clontarf,
            GymLocationDtos.astonQuay,
            GymLocationDtos.leopardstown,
            GymLocationDtos.sandymount,
            GymLocationDtos.dunLaoghaire,
            GymLocationDtos.westmanstown
        )

        val cityLocations = listOf(
            GymLocationDtos.astonQuay,
            GymLocationDtos.clontarf
        )

        val singleLocation = listOf(GymLocationDtos.clontarf)

        val emptyList = emptyList<GymLocationsDto>()
    }

    object DomainGymLocationLists {
        val allLocations = listOf(
            GymLocationsDomain.clontarf,
            GymLocationsDomain.astonQuay,
            GymLocationsDomain.leopardstown,
            GymLocationsDomain.sandymount,
            GymLocationsDomain.dunLaoghaire,
            GymLocationsDomain.westmanstown
        )

        val cityLocations = listOf(
            GymLocationsDomain.astonQuay,
            GymLocationsDomain.clontarf
        )

        val singleLocation = listOf(GymLocationsDomain.clontarf)

        val emptyList = emptyList<GymLocations>()
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("No gym locations found")
        val timeout = Exception("Request timeout")
    }
}

