package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.dto.FaultReportDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

interface FaultReportingRemoteDataSource {
    suspend fun reports(): List<FaultReportDto>

    suspend fun saveReport(report: FaultReport): FaultReportDto
}

class DefaultFaultReportingRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient,
    private val dataStoreRepository: DataStoreRepository,
) : FaultReportingRemoteDataSource {

    override suspend fun reports(): List<FaultReportDto> {
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

    override suspend fun saveReport(report: FaultReport): FaultReportDto {
        val token = authorisationToken()
        val response =
            httpClient.post(baseurl.plus(ENDPOINT)) {
                contentType(ContentType.Application.Json)
                setBody(report)
                headers { append("Authorization", "Bearer $token") }
            }

        return response.body()
    }

    private suspend fun authorisationToken(): String =
        dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""

    companion object Companion {
        private const val ENDPOINT = "/api/v1/fault"
    }
}
