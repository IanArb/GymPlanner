package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.data.clients.dto.ClientDto
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
import com.ianarbuckle.gymplanner.realm.ClientRealmDto
import com.ianarbuckle.gymplanner.realm.GymPlanRealmDto
import com.ianarbuckle.gymplanner.realm.PersonalTrainerRealmDto
import com.ianarbuckle.gymplanner.realm.SessionRealmDto
import com.ianarbuckle.gymplanner.realm.WeightRealmDto
import com.ianarbuckle.gymplanner.realm.WorkoutRealmDto
import io.realm.kotlin.ext.toRealmDictionary
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmMap

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
            personalTrainer = mapPersonalTrainer(gymPlan?.personalTrainer)
            startDate = gymPlan?.startDate ?: ""
            endDate = gymPlan?.endDate ?: ""
            sessions = mapSessions(session = gymPlan?.sessions ?: emptyList())
        }
    }

    private fun mapPersonalTrainer(personalTrainer: PersonalTrainer?): PersonalTrainerRealmDto {
        return PersonalTrainerRealmDto().apply {
            firstName = personalTrainer?.firstName ?: ""
            surname = personalTrainer?.surname ?: ""
            socials = mapSocials(personalTrainer?.socials ?: emptyMap())
        }
    }

    private fun mapSocials(socials: Map<String, String>): RealmMap<String, String> {
        return socials.toRealmDictionary()
    }

    private fun mapSessions(session: List<Session>): RealmList<SessionRealmDto> {
        return session.map { session ->
            SessionRealmDto().apply {
                name = session.name
                workouts = mapWorkouts(session.workout)
            }
        }.toRealmList()
    }

    private fun mapWorkouts(workout: List<Workout>): RealmList<WorkoutRealmDto> {
        return workout.map { workout ->
            WorkoutRealmDto().apply {
                name = workout.name
                sets = workout.sets
                repetitions = workout.repetitions
                weight = mapWeight(workout.weight)
                note = workout.note
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
            personalTrainer = transformToPersonalTrainerModel(plan?.personalTrainer),
            startDate = plan?.startDate ?: "",
            endDate = plan?.endDate ?: "",
            sessions = transformToSession(plan?.sessions ?: emptyList()),
        )
    }

    private fun transformToPersonalTrainerModel(personalTrainer: PersonalTrainerRealmDto?): PersonalTrainerDto {
        return PersonalTrainerDto(
            id = personalTrainer?.id ?: "",
            firstName = personalTrainer?.firstName ?: "",
            surname = personalTrainer?.surname ?: "",
            socials = personalTrainer?.socials ?: emptyMap()
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