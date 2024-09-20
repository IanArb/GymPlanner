package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.data.GymPlannerRepository
import com.ianarbuckle.gymplanner.model.Client

class DefaultGymPlanner(private val repository: GymPlannerRepository) : GymPlanner {

    override suspend fun fetchAllClients(): Result<List<Client>> {
        return repository.fetchClients()
    }

    override suspend fun saveClient(client: Client): Result<Client> {
        return repository.saveClient(client = client)
    }

    override suspend fun findClientById(id: String): Result<Client> {
        return repository.findClient(id)
    }

    override suspend fun deleteClient(id: String): Result<Unit> {
        return repository.deleteClient(id)
    }
}