package com.ianarbuckle.gymplanner.clients.domain

import com.ianarbuckle.gymplanner.clients.dto.ClientDto
import com.ianarbuckle.gymplanner.clients.dto.GymLocationDto
import com.ianarbuckle.gymplanner.clients.dto.GymPlanDto
import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.clients.dto.SessionDto
import com.ianarbuckle.gymplanner.clients.dto.WeightDto
import com.ianarbuckle.gymplanner.clients.dto.WorkoutDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

object ClientUiModelMapper {

    fun ClientDto.transformToClient(): Client =
        Client(
            firstName = firstName,
            surname = surname,
            strengthLevel = strengthLevel,
            gymPlan = gymPlan?.transformGymPlan(),
        )

    private fun GymPlanDto.transformGymPlan(): GymPlan =
        GymPlan(
            name = name,
            personalTrainer = personalTrainer.transformPersonalTrainer(),
            startDate = startDate,
            endDate = endDate,
            sessions = sessions.map { session -> session.transformSession() },
        )

    private fun PersonalTrainerDto.transformPersonalTrainer(): PersonalTrainer =
        PersonalTrainer(
            firstName = firstName,
            lastName = lastName,
            bio = bio,
            imageUrl = imageUrl,
            gymLocation = gymLocation.transformToGymLocation(),
            qualifications = qualifications,
            socials = socials ?: emptyMap(),
        )

    private fun GymLocationDto.transformToGymLocation(): GymLocation =
        when (this) {
            GymLocationDto.CLONTARF -> GymLocation.CLONTARF
            GymLocationDto.ASTONQUAY -> GymLocation.ASTONQUAY
            GymLocationDto.LEOPARDSTOWN -> GymLocation.LEOPARDSTOWN
            GymLocationDto.DUNLOAGHAIRE -> GymLocation.DUNLOAGHAIRE
            GymLocationDto.SANDYMOUNT -> GymLocation.SANDYMOUNT
            GymLocationDto.WESTMANSTOWN -> GymLocation.WESTMANSTOWN
            GymLocationDto.UNKNOWN -> GymLocation.UNKNOWN
        }

    private fun SessionDto.transformSession(): Session =
        Session(name = name, workout = workouts.map { workout -> workout.transformWorkout() })

    private fun WorkoutDto.transformWorkout(): Workout =
        Workout(
            name = name,
            sets = sets,
            repetitions = repetitions,
            weight = weight.transformWeight(),
            note = note,
        )

    private fun WeightDto.transformWeight(): Weight = Weight(value = value, unit = unit)
}
