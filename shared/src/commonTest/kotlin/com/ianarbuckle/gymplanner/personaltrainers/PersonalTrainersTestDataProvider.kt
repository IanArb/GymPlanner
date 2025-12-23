package com.ianarbuckle.gymplanner.personaltrainers

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.clients.dto.GymLocationDto
import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

/**
 * Provides test data for PersonalTrainersRepository tests
 */
object PersonalTrainersTestDataProvider {

    // ========== Trainer IDs ==========

    object TrainerIds {
        const val trainer1 = "pt-001"
        const val trainer2 = "pt-002"
        const val trainer3 = "pt-003"
        const val trainer4 = "pt-004"
        const val trainer5 = "pt-005"
        const val trainer6 = "pt-006"
        const val nonExistent = "pt-999"
        const val emptyId = ""
    }

    // ========== Personal Trainer DTOs ==========

    object PersonalTrainerDtos {
        val sarah = PersonalTrainerDto(
            id = TrainerIds.trainer1,
            firstName = "Sarah",
            lastName = "Murphy",
            imageUrl = "https://example.com/sarah.jpg",
            bio = "Certified personal trainer with 8 years experience specializing in strength training and nutrition. Passionate about helping clients achieve their fitness goals.",
            socials = mapOf("instagram" to "@sarahmurphy_pt", "twitter" to "@sarahfit"),
            qualifications = listOf("NASM-CPT", "Precision Nutrition Level 1", "CrossFit Level 2"),
            gymLocation = GymLocationDto.CLONTARF
        )

        val john = PersonalTrainerDto(
            id = TrainerIds.trainer2,
            firstName = "John",
            lastName = "O'Brien",
            imageUrl = "https://example.com/john.jpg",
            bio = "Former professional athlete turned personal trainer. Specializes in athletic performance and injury rehabilitation.",
            socials = mapOf("instagram" to "@johnobrien_fitness"),
            qualifications = listOf("CSCS", "ACSM-CPT", "Sports Massage Therapy"),
            gymLocation = GymLocationDto.ASTONQUAY
        )

        val emma = PersonalTrainerDto(
            id = TrainerIds.trainer3,
            firstName = "Emma",
            lastName = "Walsh",
            imageUrl = "https://example.com/emma.jpg",
            bio = "Yoga instructor and wellness coach focusing on holistic fitness and mindfulness-based training approaches.",
            socials = mapOf("instagram" to "@emmawalsh_yoga", "facebook" to "EmmaWalshWellness"),
            qualifications = listOf("RYT-500", "Pilates Instructor", "Nutrition Coach"),
            gymLocation = GymLocationDto.LEOPARDSTOWN
        )

        val michael = PersonalTrainerDto(
            id = TrainerIds.trainer4,
            firstName = "Michael",
            lastName = "Kelly",
            imageUrl = "https://example.com/michael.jpg",
            bio = "High-intensity interval training specialist with focus on weight loss and cardiovascular fitness.",
            socials = null,
            qualifications = listOf("ACE-CPT", "HIIT Specialist", "TRX Certified"),
            gymLocation = GymLocationDto.SANDYMOUNT
        )

        val lisa = PersonalTrainerDto(
            id = TrainerIds.trainer5,
            firstName = "Lisa",
            lastName = "Ryan",
            imageUrl = "https://example.com/lisa.jpg",
            bio = "Pre and postnatal fitness specialist helping new mothers regain strength and confidence.",
            socials = mapOf("instagram" to "@lisaryan_pt"),
            qualifications = listOf("NASM-CPT", "Pre/Postnatal Specialist", "Corrective Exercise Specialist"),
            gymLocation = GymLocationDto.DUNLOAGHAIRE
        )

        val david = PersonalTrainerDto(
            id = TrainerIds.trainer6,
            firstName = "David",
            lastName = "McCarthy",
            imageUrl = "https://example.com/david.jpg",
            bio = "Bodybuilding coach and nutrition expert helping clients build muscle and transform their physiques.",
            socials = mapOf("instagram" to "@davidmccarthy_bb", "youtube" to "DavidMcCarthyFitness"),
            qualifications = listOf("ISSA-CFT", "Bodybuilding Specialist", "Sports Nutritionist"),
            gymLocation = GymLocationDto.WESTMANSTOWN
        )
    }

    // ========== Personal Trainers (Domain) ==========

    object PersonalTrainers {
        val sarah = PersonalTrainer(
            id = TrainerIds.trainer1,
            firstName = "Sarah",
            lastName = "Murphy",
            imageUrl = "https://example.com/sarah.jpg",
            bio = "Certified personal trainer with 8 years experience specializing in strength training and nutrition. Passionate about helping clients achieve their fitness goals.",
            socials = mapOf("instagram" to "@sarahmurphy_pt", "twitter" to "@sarahfit"),
            qualifications = listOf("NASM-CPT", "Precision Nutrition Level 1", "CrossFit Level 2"),
            gymLocation = GymLocation.CLONTARF
        )

        val john = PersonalTrainer(
            id = TrainerIds.trainer2,
            firstName = "John",
            lastName = "O'Brien",
            imageUrl = "https://example.com/john.jpg",
            bio = "Former professional athlete turned personal trainer. Specializes in athletic performance and injury rehabilitation.",
            socials = mapOf("instagram" to "@johnobrien_fitness"),
            qualifications = listOf("CSCS", "ACSM-CPT", "Sports Massage Therapy"),
            gymLocation = GymLocation.ASTONQUAY
        )

        val emma = PersonalTrainer(
            id = TrainerIds.trainer3,
            firstName = "Emma",
            lastName = "Walsh",
            imageUrl = "https://example.com/emma.jpg",
            bio = "Yoga instructor and wellness coach focusing on holistic fitness and mindfulness-based training approaches.",
            socials = mapOf("instagram" to "@emmawalsh_yoga", "facebook" to "EmmaWalshWellness"),
            qualifications = listOf("RYT-500", "Pilates Instructor", "Nutrition Coach"),
            gymLocation = GymLocation.LEOPARDSTOWN
        )

        val michael = PersonalTrainer(
            id = TrainerIds.trainer4,
            firstName = "Michael",
            lastName = "Kelly",
            imageUrl = "https://example.com/michael.jpg",
            bio = "High-intensity interval training specialist with focus on weight loss and cardiovascular fitness.",
            socials = emptyMap(),
            qualifications = listOf("ACE-CPT", "HIIT Specialist", "TRX Certified"),
            gymLocation = GymLocation.SANDYMOUNT
        )

        val lisa = PersonalTrainer(
            id = TrainerIds.trainer5,
            firstName = "Lisa",
            lastName = "Ryan",
            imageUrl = "https://example.com/lisa.jpg",
            bio = "Pre and postnatal fitness specialist helping new mothers regain strength and confidence.",
            socials = mapOf("instagram" to "@lisaryan_pt"),
            qualifications = listOf("NASM-CPT", "Pre/Postnatal Specialist", "Corrective Exercise Specialist"),
            gymLocation = GymLocation.DUNLOAGHAIRE
        )

        val david = PersonalTrainer(
            id = TrainerIds.trainer6,
            firstName = "David",
            lastName = "McCarthy",
            imageUrl = "https://example.com/david.jpg",
            bio = "Bodybuilding coach and nutrition expert helping clients build muscle and transform their physiques.",
            socials = mapOf("instagram" to "@davidmccarthy_bb", "youtube" to "DavidMcCarthyFitness"),
            qualifications = listOf("ISSA-CFT", "Bodybuilding Specialist", "Sports Nutritionist"),
            gymLocation = GymLocation.WESTMANSTOWN
        )
    }

    // ========== Personal Trainer Lists ==========

    object PersonalTrainerLists {
        val clontarfTrainers = listOf(PersonalTrainerDtos.sarah)

        val astonQuayTrainers = listOf(PersonalTrainerDtos.john)

        val allTrainers = listOf(
            PersonalTrainerDtos.sarah,
            PersonalTrainerDtos.john,
            PersonalTrainerDtos.emma,
            PersonalTrainerDtos.michael,
            PersonalTrainerDtos.lisa,
            PersonalTrainerDtos.david
        )

        val emptyList = emptyList<PersonalTrainerDto>()
    }

    object DomainPersonalTrainerLists {
        val clontarfTrainers = listOf(PersonalTrainers.sarah)

        val astonQuayTrainers = listOf(PersonalTrainers.john)

        val allTrainers = listOf(
            PersonalTrainers.sarah,
            PersonalTrainers.john,
            PersonalTrainers.emma,
            PersonalTrainers.michael,
            PersonalTrainers.lisa,
            PersonalTrainers.david
        )

        val emptyList = emptyList<PersonalTrainer>()
    }

    // ========== Exceptions ==========

    object Exceptions {
        val networkError = Exception("Network unavailable")
        val serverError = RuntimeException("Internal server error")
        val unauthorized = RuntimeException("Unauthorized - invalid token")
        val notFound = RuntimeException("Personal trainer not found")
        val timeout = Exception("Request timeout")
        val invalidLocation = IllegalArgumentException("Invalid gym location")
    }
}

