package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.model.GymPlan
import com.ianarbuckle.gymplanner.model.PersonalTrainer
import com.ianarbuckle.gymplanner.model.Session
import com.ianarbuckle.gymplanner.model.Workout
import com.ianarbuckle.gymplanner.realm.ClientRealmDto
import com.ianarbuckle.gymplanner.realm.GymPlanRealmDto
import com.ianarbuckle.gymplanner.realm.PersonalTrainerRealmDto
import com.ianarbuckle.gymplanner.realm.SessionRealmDto
import com.ianarbuckle.gymplanner.realm.WorkoutRealmDto
import io.realm.kotlin.ext.toRealmDictionary
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmMap

class ClientMapper {

    fun mapGymPlan(gymPlan: GymPlan?): GymPlanRealmDto {
        return GymPlanRealmDto().apply {
            name = gymPlan?.name ?: ""
            personalTrainer = mapPersonalTrainer(gymPlan?.personalTrainer)
            startDate = gymPlan?.startDate ?: ""
            endDate = gymPlan?.endDate ?: ""
            sessions = mapSessions(sessions = gymPlan?.sessions ?: emptyList())
        }
    }

    private fun mapPersonalTrainer(personalTrainer: PersonalTrainer?): PersonalTrainerRealmDto {
        return PersonalTrainerRealmDto().apply {
            id = personalTrainer?.id ?: ""
            name = personalTrainer?.name ?: ""
            socials = mapSocials(personalTrainer?.socials ?: emptyMap())
        }
    }

    private fun mapSocials(socials: Map<String, String>): RealmMap<String, String> {
        return socials.toRealmDictionary()
    }

    private fun mapSessions(sessions: List<Session>): RealmList<SessionRealmDto> {
        return sessions.map { session ->
            SessionRealmDto().apply {
                name = session.name
                workouts = mapWorkouts(session.workouts)
            }
        }.toRealmList()
    }

    private fun mapWorkouts(workouts: List<Workout>): RealmList<WorkoutRealmDto> {
        return workouts.map { workout ->
            WorkoutRealmDto().apply {
                name = workout.name
                sets = workout.sets
                repetitions = workout.repetitions
                weight = workout.weight
                note = workout.note
            }
        }.toRealmList()
    }

    fun transformToClientPlan(clientDto: ClientRealmDto): Client {
        return Client(
            id = clientDto.id.toString(),
            firstName = clientDto.firstName,
            surname = clientDto.surname,
            strengthLevel = clientDto.strengthLevel,
            gymPlan = transformToGymPlanModel(clientDto.gymPlan)
        )
    }

    private fun transformToSession(sessions: List<SessionRealmDto>): List<Session> {
        return sessions.map { session ->
            Session(
                name = session.name,
                workouts = transformWorkouts(session.workouts)
            )
        }
    }

    private fun transformToGymPlanModel(plan: GymPlanRealmDto?): GymPlan {
        return GymPlan(
            name = plan?.name ?: "",
            personalTrainer = transformToPersonalTrainerModel(plan?.personalTrainer),
            startDate = plan?.startDate ?: "",
            endDate = plan?.endDate ?: "",
            sessions = transformToSession(plan?.sessions ?: emptyList()),
        )
    }

    private fun transformToPersonalTrainerModel(personalTrainer: PersonalTrainerRealmDto?): PersonalTrainer {
        return PersonalTrainer(
            id = personalTrainer?.id ?: "",
            name = personalTrainer?.name ?: "",
            socials = personalTrainer?.socials ?: emptyMap()
        )
    }

    fun transformClientDto(clients: ResultsChange<ClientRealmDto>): List<Client> {
        return clients.list.map { client ->
            Client(
                id = client.id.toString(),
                firstName = client.firstName,
                surname = client.surname,
                strengthLevel = client.strengthLevel,
                gymPlan = transformToGymPlanModel(client.gymPlan)
            )
        }
    }

    private fun transformWorkouts(workouts: List<WorkoutRealmDto>): List<Workout> {
        return workouts.map { workout ->
            Workout(
                name = workout.name,
                sets = workout.sets,
                repetitions = workout.repetitions,
                weight = workout.weight,
                note = workout.note,
            )
        }
    }
}