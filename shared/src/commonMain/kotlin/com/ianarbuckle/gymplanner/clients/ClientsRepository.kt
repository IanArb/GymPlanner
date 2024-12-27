package com.ianarbuckle.gymplanner.clients

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.ClientUiModelMapper.transformToClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import okio.IOException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ClientsRepository {
    suspend fun saveClient(client: Client): Result<Client>
    suspend fun fetchClients(): Result<ImmutableList<Client>>
    suspend fun findClient(id: String): Result<Client>
    suspend fun deleteClient(id: String): Result<Unit>
}

class DefaultClientsRepository : ClientsRepository, KoinComponent {

    private val localDataSource: ClientsLocalDataSource by inject()
    private val remoteDataSource: ClientsRemoteDataSource by inject()

    override suspend fun saveClient(client: Client): Result<Client> {
        try {
            remoteDataSource.saveClient(client)
            localDataSource.saveGymPlan(client)
            return Result.success(client)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }

    override suspend fun fetchClients(): Result<ImmutableList<Client>> {
        try {
            val remoteClients = remoteDataSource.clients()
            val clients = remoteClients.map { client ->
                val transformToClient = client.transformToClient()
                localDataSource.saveGymPlan(transformToClient)
                transformToClient
            }
            return Result.success(clients.toImmutableList())
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            return Result.failure(ex)
        }
    }

    override suspend fun findClient(id: String): Result<Client> {
        try {
            val client = remoteDataSource.clientById(id)
            return Result.success(client.transformToClient())
        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun deleteClient(id: String): Result<Unit> {
        try {
            remoteDataSource.deleteClient(id)
            localDataSource.deleteClient(id)
            return Result.success(Unit)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("ClientsRepository").e("Error deleting client: $ex")
            return Result.failure(ex)
        }
    }

}