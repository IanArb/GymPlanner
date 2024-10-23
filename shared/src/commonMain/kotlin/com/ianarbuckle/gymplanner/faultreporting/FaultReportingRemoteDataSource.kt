package com.ianarbuckle.gymplanner.faultreporting

import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.faultreporting.dto.FaultReportDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path

class FaultReportingRemoteDataSource(
    private val baseurl: String,
    private val httpClient: HttpClient
) {

    suspend fun reports(): List<FaultReportDto> {
        val response = httpClient.get(baseurl) {
            url {
                protocol = URLProtocol.HTTPS
                path(ENDPOINT)
            }
        }

        return response.body()
    }

    suspend fun saveReport(report: FaultReport): FaultReportDto {
        val response = httpClient.post(baseurl.plus(ENDPOINT)) {
            contentType(ContentType.Application.Json)
            setBody(report)
        }

        return response.body()
    }

    companion object {
        private const val ENDPOINT = "/api/v1/fault"
    }
}