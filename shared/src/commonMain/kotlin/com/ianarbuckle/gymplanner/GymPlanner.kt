package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.model.GymPlan
import kotlinx.coroutines.flow.Flow

interface GymPlanner {

    suspend fun fetchAllClients(): Flow<List<Client>>

    suspend fun saveClient(client: Client)

    fun findClientById(id: String): Client
}