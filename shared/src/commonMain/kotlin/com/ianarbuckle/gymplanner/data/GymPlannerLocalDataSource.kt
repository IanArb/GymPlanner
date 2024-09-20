package com.ianarbuckle.gymplanner.data

import com.ianarbuckle.gymplanner.mapper.ClientMapper
import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.realm.ClientRealmDto
import com.ianarbuckle.gymplanner.realm.GymPlanRealmDto
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GymPlannerLocalDataSource(
    private val realm: Realm,
    private val mapper: ClientMapper
) {

    suspend fun saveGymPlan(client: Client) {
        val realmClient = ClientRealmDto().apply {
            firstName = client.firstName
            surname = client.surname
            strengthLevel = client.strengthLevel
            gymPlan = mapper.mapGymPlan(client.gymPlan)
        }

        realm.write {
            this.copyToRealm(realmClient)
        }
    }

    fun findClientById(id: String): Client {
        val filterByPrimaryKey = realm.query<ClientRealmDto>("_id == $id")
        val client = filterByPrimaryKey.find().first()

        return mapper.transformToClientPlan(client)
    }

    fun findAllClients(): Flow<List<Client>> {
        return realm.query<ClientRealmDto>().asFlow()
            .map {
                when (it) {
                    is InitialResults -> {
                        mapper.transformClientDto(clients = it)
                    }
                    is UpdatedResults -> {
                        mapper.transformClientDto(clients = it)
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