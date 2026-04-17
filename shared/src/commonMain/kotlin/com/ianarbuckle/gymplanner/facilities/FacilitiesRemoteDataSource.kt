package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.dto.FacilityStatusDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.ktor.http.path

interface FacilitiesRemoteDataSource {
    suspend fun findMachinesByGymLocation(gymLocation: String): List<FacilityStatusDto>
}

class DefaultFacilitiesRemoteDataSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
) : FacilitiesRemoteDataSource {

    override suspend fun findMachinesByGymLocation(gymLocation: String): List<FacilityStatusDto> {
        val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
        val response =
            httpClient.get(baseUrl) {
                url {
                    protocol = URLProtocol.HTTPS
                    path(FACILITY_STATUS_ENDPOINT)
                    parameters.append("gymLocation", gymLocation)
                }
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    companion object Companion {
        private const val FACILITY_STATUS_ENDPOINT = "/api/v1/facilities"
    }
}
