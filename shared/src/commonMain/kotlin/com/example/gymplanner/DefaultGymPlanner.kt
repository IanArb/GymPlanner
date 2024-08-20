package com.example.gymplanner

import com.example.gymplanner.data.GymPlannerRepository
import com.example.gymplanner.model.GymPlan
import kotlinx.coroutines.flow.Flow

class DefaultGymPlanner(private val repository: GymPlannerRepository) : GymPlanner {

    override suspend fun fetchAllGymPlans(): Flow<List<GymPlan>> {
        return repository.fetchGymPlans()
    }

    override suspend fun saveGymPlan(gymPlan: GymPlan) {
        repository.saveGymPlan(gymPlan = gymPlan)
    }

    override fun findGymPlanById(id: String): GymPlan {
        return repository.findGymPlan(id)
    }
}