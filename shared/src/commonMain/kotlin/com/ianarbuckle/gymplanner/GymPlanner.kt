package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.model.GymPlan
import kotlinx.coroutines.flow.Flow

interface GymPlanner {

    suspend fun fetchAllGymPlans(): Flow<List<GymPlan>>

    suspend fun saveGymPlan(plan: GymPlan)

    fun findGymPlanById(id: String): GymPlan
}