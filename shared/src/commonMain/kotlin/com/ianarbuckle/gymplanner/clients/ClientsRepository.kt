package com.ianarbuckle.gymplanner.clients

import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.ClientUiModelMapper.transformToClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

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

    suspend fun fetchClients(): Result<ImmutableList<Client>> {
        try {
            val remoteClients = remoteDataSource.clients()
            val clients = remoteClients.map { client ->
                val transformToClient = client.transformToClient()
                localDataSource.saveGymPlan(transformToClient)
                transformToClient
            }
            return Result.success(clients.toImmutableList())
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