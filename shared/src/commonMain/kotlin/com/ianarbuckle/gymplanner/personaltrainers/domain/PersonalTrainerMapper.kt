package com.ianarbuckle.gymplanner.personaltrainers.domain

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.clients.dto.GymLocationDto
import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto

object PersonalTrainerMapper {

    fun PersonalTrainerDto.toPersonalTrainer(): PersonalTrainer {
        return PersonalTrainer(
            id = id,
            firstName = firstName,
            lastName = lastName,
            imageUrl = imageUrl,
            bio = bio,
            socials = socials ?: emptyMap(),
            qualifications = qualifications,
            gymLocation = gymLocation.toGymLocation(),
        )
    }

    private fun GymLocationDto.toGymLocation(): GymLocation {
        return this.let {
            when (it) {
                GymLocationDto.CLONTARF -> GymLocation.CLONTARF
                GymLocationDto.ASTONQUAY -> GymLocation.ASTONQUAY
                GymLocationDto.LEOPARDSTOWN -> GymLocation.LEOPARDSTOWN
                GymLocationDto.DUNLOAGHAIRE -> GymLocation.DUNLOAGHAIRE
                GymLocationDto.SANDYMOUNT -> GymLocation.SANDYMOUNT
                GymLocationDto.WESTMANSTOWN -> GymLocation.WESTMANSTOWN
                GymLocationDto.UNKNOWN -> GymLocation.UNKNOWN
            }
        }
    }
}
