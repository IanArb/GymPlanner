package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.model.GymPlan
import kotlinx.coroutines.flow.Flow

interface GymPlanner {

    suspend fun fetchAllClients(): Result<List<Client>>

    suspend fun saveClient(client: Client): Result<Client>

    suspend fun findClientById(id: String): Result<Client>

    suspend fun deleteClient(id: String): Result<Unit>
}