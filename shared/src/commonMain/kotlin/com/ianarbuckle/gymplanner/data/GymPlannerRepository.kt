package com.ianarbuckle.gymplanner.data

import com.ianarbuckle.gymplanner.model.Client
import kotlinx.coroutines.flow.Flow

class GymPlannerRepository(private val localDataSource: GymPlannerLocalDataSource) {

    suspend fun saveClient(client: Client) {
        return localDataSource.saveGymPlan(client)
    }

    fun fetchClients(): Flow<List<Client>> = localDataSource.findAllClients()

    fun findClient(id: String): Client = localDataSource.findClientById(id)

    suspend fun deleteAllClients() {
        localDataSource.deleteAllClients()
    }

}