package com.ianarbuckle.gymplanner.clients

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.ClientUiModelMapper.transformToClient
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface ClientsRepository {
  suspend fun saveClient(client: Client): Result<Client>

  suspend fun fetchClients(): Result<ImmutableList<Client>>

  suspend fun findClient(id: String): Result<Client>

  suspend fun deleteClient(id: String): Result<Unit>
}

class DefaultClientsRepository : ClientsRepository, KoinComponent {

  private val remoteDataSource: ClientsRemoteDataSource by inject()

  override suspend fun saveClient(client: Client): Result<Client> {
    try {
      remoteDataSource.saveClient(client)
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
      val clients =
        remoteClients.map { client ->
          val transformToClient = client.transformToClient()
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
