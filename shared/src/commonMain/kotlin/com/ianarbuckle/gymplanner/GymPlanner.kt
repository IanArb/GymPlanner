package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.model.FaultReport
import com.ianarbuckle.gymplanner.model.FitnessClass

interface GymPlanner {

    suspend fun fetchAllClients(): Result<List<Client>>

    suspend fun saveClient(client: Client): Result<Client>

    suspend fun findClientById(id: String): Result<Client>

    suspend fun deleteClient(id: String): Result<Unit>

    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>>

    suspend fun fetchTodaysFitnessClasses(): Result<List<FitnessClass>>

    suspend fun submitFault(fault: FaultReport): Result<FaultReport>
}