package com.ianarbuckle.gymplanner.data.clients.clients

import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.model.dto.ClientDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class ClientsRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient
) {

    suspend fun clients(): List<ClientDto> {
        val response = httpClient.get(baseurl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
            }
        }

        return response.body()
    }

    suspend fun clientById(id: String): ClientDto {
        val response = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = baseurl
                path("$ENDPOINT/$id")
            }
        }

        return response.body()
    }

    suspend fun saveClient(client: Client): ClientDto {
        val response = httpClient.post(baseurl.plus(ENDPOINT)) {
            contentType(ContentType.Application.Json)
            setBody(client)
        }

        return response.body()
    }

    suspend fun updateClient(clientDto: ClientDto) {
        httpClient.put {
            contentType(ContentType.Application.Json)
            setBody(clientDto)
        }
    }

    suspend fun deleteClient(id: String) {
        httpClient.delete {
            url {
                protocol = URLProtocol.HTTPS
                host = baseurl
                path("$ENDPOINT/$id")
            }
        }
    }

    companion object {
        private const val ENDPOINT = "/api/v1/clients"
    }

}