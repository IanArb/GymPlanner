package com.ianarbuckle.gymplanner.clients.domain

import com.ianarbuckle.gymplanner.clients.dto.ClientDto
import com.ianarbuckle.gymplanner.clients.dto.ClientRealmDto
import com.ianarbuckle.gymplanner.clients.dto.GymPlanDto
import com.ianarbuckle.gymplanner.clients.dto.GymPlanRealmDto
import com.ianarbuckle.gymplanner.clients.dto.SessionDto
import com.ianarbuckle.gymplanner.clients.dto.SessionRealmDto
import com.ianarbuckle.gymplanner.clients.dto.WeightDto
import com.ianarbuckle.gymplanner.clients.dto.WeightRealmDto
import com.ianarbuckle.gymplanner.clients.dto.WorkoutDto
import com.ianarbuckle.gymplanner.clients.dto.WorkoutRealmDto
import com.ianarbuckle.gymplanner.personaltrainers.domain.PersonalTrainerMapper
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmList

object ClientMapper {

    fun mapClient(client: Client): ClientRealmDto {
        return ClientRealmDto().apply {
            firstName = client.firstName
            surname = client.surname
            strengthLevel = client.strengthLevel
            gymPlan = mapGymPlan(client.gymPlan)
        }
    }

    private fun mapGymPlan(gymPlan: GymPlan?): GymPlanRealmDto {
        return GymPlanRealmDto().apply {
            name = gymPlan?.name ?: ""
            personalTrainer = PersonalTrainerMapper.mapPersonalTrainer(gymPlan?.personalTrainer)
            startDate = gymPlan?.startDate ?: ""
            endDate = gymPlan?.endDate ?: ""
            sessions = mapSessions(session = gymPlan?.sessions ?: emptyList())
        }
    }

    private fun mapSessions(session: List<Session>): RealmList<SessionRealmDto> {
        return session.map {
            SessionRealmDto().apply {
                name = it.name
                workouts = mapWorkouts(it.workout)
            }
        }.toRealmList()
    }

    private fun mapWorkouts(workout: List<Workout>): RealmList<WorkoutRealmDto> {
        return workout.map {
            WorkoutRealmDto().apply {
                name = it.name
                sets = it.sets
                repetitions = it.repetitions
                weight = mapWeight(it.weight)
                note = it.note
            }
        }.toRealmList()
    }

    private fun mapWeight(weight: Weight): WeightRealmDto {
        return WeightRealmDto().apply {
            value = weight.value
            unit = weight.unit
        }
    }

    fun transformToClientPlan(clientDto: ClientRealmDto): ClientDto {
        return ClientDto(
            id = clientDto.id.toString(),
            firstName = clientDto.firstName,
            surname = clientDto.surname,
            strengthLevel = clientDto.strengthLevel,
            gymPlan = transformToGymPlanModel(clientDto.gymPlan)
        )
    }

    private fun transformToSession(sessions: List<SessionRealmDto>): List<SessionDto> {
        return sessions.map { session ->
            SessionDto(
                name = session.name,
                workouts = transformWorkouts(session.workouts)
            )
        }
    }

    private fun transformToGymPlanModel(plan: GymPlanRealmDto?): GymPlanDto {
        return GymPlanDto(
            name = plan?.name ?: "",
            personalTrainer = PersonalTrainerMapper.transformToPersonalTrainerModel(plan?.personalTrainer),
            startDate = plan?.startDate ?: "",
            endDate = plan?.endDate ?: "",
            sessions = transformToSession(plan?.sessions ?: emptyList()),
        )
    }

    fun transformClientDto(clients: ResultsChange<ClientRealmDto>): List<ClientDto> {
        return clients.list.map { client ->
            ClientDto(
                id = client.id.toString(),
                firstName = client.firstName,
                surname = client.surname,
                strengthLevel = client.strengthLevel,
                gymPlan = transformToGymPlanModel(client.gymPlan)
            )
        }
    }

    private fun transformWorkouts(workouts: List<WorkoutRealmDto>): List<WorkoutDto> {
        return workouts.map { workout ->
            WorkoutDto(
                name = workout.name,
                sets = workout.sets,
                repetitions = workout.repetitions,
                weight = mapWeight(weight = workout.weight),
                note = workout.note,
            )
        }
    }

    private fun mapWeight(weight: WeightRealmDto?): WeightDto {
        return WeightDto(
            value = weight?.value ?: 0.0,
            unit = weight?.unit ?: ""
        )
    }
}