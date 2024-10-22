package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.data.clients.dto.ClientDto
import com.ianarbuckle.gymplanner.data.clients.dto.GymLocationDto
import com.ianarbuckle.gymplanner.model.GymPlan
import com.ianarbuckle.gymplanner.data.clients.dto.GymPlanDto
import com.ianarbuckle.gymplanner.model.PersonalTrainer
import com.ianarbuckle.gymplanner.data.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.model.Session
import com.ianarbuckle.gymplanner.data.clients.dto.SessionDto
import com.ianarbuckle.gymplanner.model.Weight
import com.ianarbuckle.gymplanner.data.clients.dto.WeightDto
import com.ianarbuckle.gymplanner.model.Workout
import com.ianarbuckle.gymplanner.data.clients.dto.WorkoutDto
import com.ianarbuckle.gymplanner.model.GymLocation

object ClientUiModelMapper {

    fun ClientDto.transformToClient(): Client {
        return Client(
            firstName = firstName,
            surname = surname,
            strengthLevel = strengthLevel,
            gymPlan = gymPlan?.transformGymPlan()
        )
    }

    private fun GymPlanDto.transformGymPlan(): GymPlan {
        return GymPlan(
            name = name,
            personalTrainer = personalTrainer.transformPersonalTrainer(),
            startDate = startDate,
            endDate = endDate,
            sessions = sessions.map { session ->
                session.transformSession()
            }
        )
    }

    private fun PersonalTrainerDto.transformPersonalTrainer(): PersonalTrainer {
        return PersonalTrainer(
            firstName = firstName,
            lastName = lastName,
            bio = bio,
            imageUrl = imageUrl,
            gymLocation = gymLocation.transformToGymLocation(),
            socials = socials
        )
    }

    private fun GymLocationDto.transformToGymLocation(): GymLocation {
        return when (this) {
            GymLocationDto.CLONTARF -> GymLocation.CLONTARF
            GymLocationDto.ASTONQUAY -> GymLocation.ASTONQUAY
            GymLocationDto.LEOPARDSTOWN -> GymLocation.LEOPARDSTOWN
            GymLocationDto.DUNLOAGHAIRE -> GymLocation.DUNLOAGHAIRE
            GymLocationDto.UNKNOWN -> GymLocation.UNKNOWN
        }
    }

    private fun SessionDto.transformSession(): Session {
        return Session(
            name = name,
            workout = workouts.map { workout ->
                workout.transformWorkout()
            }
        )
    }

    private fun WorkoutDto.transformWorkout(): Workout {
        return Workout(
            name = name,
            sets = sets,
            repetitions = repetitions,
            weight = weight.transformWeight(),
            note = note
        )
    }

    private fun WeightDto.transformWeight(): Weight {
        return Weight(
            value = value,
            unit = unit
        )
    }
}