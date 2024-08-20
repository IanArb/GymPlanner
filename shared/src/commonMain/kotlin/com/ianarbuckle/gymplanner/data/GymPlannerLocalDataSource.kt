package com.ianarbuckle.gymplanner.data

import com.ianarbuckle.gymplanner.model.GymPlan
import com.ianarbuckle.gymplanner.model.Workout
import com.ianarbuckle.gymplanner.realm.GymPlanRealmDto
import com.ianarbuckle.gymplanner.realm.WorkoutRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GymPlannerLocalDataSource(private val realm: Realm) {

    suspend fun saveGymPlan(gymPlan: GymPlan) {
        val gymDb = GymPlanRealmDto().apply {
            name = gymPlan.name
            startDate = gymPlan.startDate
            endDate = gymPlan.endDate
            workouts = mapWorkouts(gymPlan.workouts)
        }

        realm.write {
            this.copyToRealm(gymDb)
        }
    }

    private fun mapWorkouts(workouts: List<Workout>): List<WorkoutRealmDto> {
        return workouts.map { workout ->
            WorkoutRealmDto().apply {
                name = workout.name
                sets = workout.sets
                repetitions = workout.repetitions
                weight = workout.weight
                note = workout.note
            }
        }
    }

    fun findAllGymPlans(): Flow<List<GymPlan>> {
        return realm.query<GymPlanRealmDto>().asFlow()
            .map {
                when (it) {
                    is InitialResults -> {
                        transformToGymPlanModel(model = it)
                    }
                    is UpdatedResults -> {
                        transformToGymPlanModel(model = it)
                    }
                }
            }
    }

    fun findGymPlanById(id: String): GymPlan {
        val filterByPrimaryKey = realm.query<GymPlanRealmDto>("_id == $id")
        val findPrimaryKey = filterByPrimaryKey.find().first()

        return transformToGymPlan(findPrimaryKey)
    }

    private fun transformToGymPlan(gymPlanRealmDto: GymPlanRealmDto): GymPlan {
        return GymPlan(
            id = gymPlanRealmDto.id.toString(),
            name = gymPlanRealmDto.name,
            startDate = gymPlanRealmDto.startDate,
            endDate = gymPlanRealmDto.endDate,
            workouts = transformWorkouts(gymPlanRealmDto.workouts)
        )
    }

    private fun transformToGymPlanModel(model: ResultsChange<GymPlanRealmDto>): List<GymPlan> {
        return model.list.map { plan ->
            GymPlan(
                id = plan.id.toString(),
                name = plan.name,
                startDate = plan.startDate,
                endDate = plan.endDate,
                workouts = transformWorkouts(plan.workouts)
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