package com.ianarbuckle.gymplanner.clients

import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.ClientMapper
import com.ianarbuckle.gymplanner.clients.dto.ClientDto
import com.ianarbuckle.gymplanner.clients.dto.ClientRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClientsLocalDataSource(
    private val realm: Realm,
) {

    suspend fun saveGymPlan(client: Client) {
        realm.write {
            this.copyToRealm(ClientMapper.mapClient(client))
        }
    }

    fun findClientById(id: String): ClientDto {
        val filterByPrimaryKey = realm.query<ClientRealmDto>("_id == $id")
        val client = filterByPrimaryKey.find().first()

        return ClientMapper.transformToClientPlan(client)
    }

    fun findAllClients(): Flow<List<ClientDto>> {
        return realm.query<ClientRealmDto>().asFlow()
            .map {
                when (it) {
                    is InitialResults -> {
                        ClientMapper.transformClientDto(clients = it)
                    }
                    is UpdatedResults -> {
                        ClientMapper.transformClientDto(clients = it)
                    }
                }
            }
    }

    suspend fun deleteClient(id: String) {
        val filterByPrimaryKey = realm.query<ClientRealmDto>("_id == $id")
        val client = filterByPrimaryKey.find().first()

        realm.write {
            delete(client)
        }
    }

    suspend fun deleteAllClients() {
        realm.write {
            val query = this.query<ClientRealmDto>()

            val results = query.find()
            delete(results)

            results.forEach { clients ->
                delete(clients)
            }
        }
    }
}
