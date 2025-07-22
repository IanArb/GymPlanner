package com.ianarbuckle.gymplanner.clients

import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.dto.ClientDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class ClientsRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
) {

    suspend fun clients(): List<ClientDto> {
        val token = authorisationToken()
        val response =
            httpClient.get(baseurl) {
                url {
                    protocol = URLProtocol.HTTPS
                    path(ENDPOINT)
                }
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    suspend fun clientById(id: String): ClientDto {
        val token = authorisationToken()
        val response =
            httpClient.get {
                url {
                    protocol = URLProtocol.HTTPS
                    host = baseurl
                    path("$ENDPOINT/$id")
                }
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    suspend fun saveClient(client: Client): ClientDto {
        val token = authorisationToken()
        val response =
            httpClient.post(baseurl.plus(ENDPOINT)) {
                contentType(ContentType.Application.Json)
                setBody(client)
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    suspend fun updateClient(clientDto: ClientDto) {
        val token = authorisationToken()
        httpClient.put {
            contentType(ContentType.Application.Json)
            setBody(clientDto)
            headers { append("Authorization", "Bearer $token") }
        }
    }

    suspend fun deleteClient(id: String) {
        val token = authorisationToken()
        httpClient.delete {
            url {
                protocol = URLProtocol.HTTPS
                host = baseurl
                path("$ENDPOINT/$id")
            }
            headers { append("Authorization", "Bearer $token") }
        }
    }

    private suspend fun authorisationToken() =
        dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""

    companion object {
        private const val ENDPOINT = "/api/v1/clients"
    }
}
