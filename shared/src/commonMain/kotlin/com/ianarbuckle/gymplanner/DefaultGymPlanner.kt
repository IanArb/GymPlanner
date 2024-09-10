package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.data.GymPlannerRepository
import com.ianarbuckle.gymplanner.model.Client
import kotlinx.coroutines.flow.Flow

class DefaultGymPlanner(private val repository: GymPlannerRepository) : GymPlanner {

    override suspend fun fetchAllClients(): Flow<List<Client>> {
        return repository.fetchClients()
    }

    override suspend fun saveClient(client: Client) {
        repository.saveClient(client = client)
    }

    override fun findClientById(id: String): Client {
        return repository.findClient(id)
    }
}