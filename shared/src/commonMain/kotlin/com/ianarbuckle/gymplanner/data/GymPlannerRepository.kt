package com.ianarbuckle.gymplanner.data

import com.ianarbuckle.gymplanner.model.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GymPlannerRepository(
    private val localDataSource: GymPlannerLocalDataSource,
    private val remoteDataSource: GymPlannerRemoteDataSource
) {

    suspend fun saveClient(client: Client): Result<Client> {
        try {
            remoteDataSource.saveClient(client)
            localDataSource.saveGymPlan(client)
            return Result.success(client)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun fetchClients(): Result<List<Client>> {
        try {
            val remoteClients = remoteDataSource.clients()
            remoteClients.map { client ->
                localDataSource.saveGymPlan(client)
            }
            return Result.success(remoteClients)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    suspend fun findClient(id: String): Result<Client> {
        try {
            val client = remoteDataSource.clientById(id)
            return Result.success(client)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    suspend fun deleteClient(id: String): Result<Unit> {
        try {
            remoteDataSource.deleteClient(id)
            localDataSource.deleteClient(id)
            return Result.success(Unit)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

}