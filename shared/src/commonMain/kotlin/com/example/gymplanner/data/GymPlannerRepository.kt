package com.example.gymplanner.data

import com.example.gymplanner.model.GymPlan
import kotlinx.coroutines.flow.Flow

class GymPlannerRepository(private val localDataSource: GymPlannerLocalDataSource) {

    suspend fun saveGymPlan(gymPlan: GymPlan) {
        return localDataSource.saveGymPlan(gymPlan)
    }

    fun fetchGymPlans(): Flow<List<GymPlan>> = localDataSource.findAllGymPlans()

    fun findGymPlan(id: String): GymPlan = localDataSource.findGymPlanById(id)

}