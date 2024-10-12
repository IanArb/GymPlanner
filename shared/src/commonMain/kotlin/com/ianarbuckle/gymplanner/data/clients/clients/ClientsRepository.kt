package com.ianarbuckle.gymplanner.data.clients.clients

import com.ianarbuckle.gymplanner.mapper.ClientUiModelMapper.transformToClient
import com.ianarbuckle.gymplanner.model.Client

class ClientsRepository(
    private val localDataSource: ClientsLocalDataSource,
    private val remoteDataSource: ClientsRemoteDataSource,
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
            val clients = remoteClients.map { client ->
                val transformToClient = client.transformToClient()
                localDataSource.saveGymPlan(transformToClient)
                transformToClient
            }
            return Result.success(clients)
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    suspend fun findClient(id: String): Result<Client> {
        try {
            val client = remoteDataSource.clientById(id)
            return Result.success(client.transformToClient())
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