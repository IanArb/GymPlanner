package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.dto.BookingDto
import com.ianarbuckle.gymplanner.booking.dto.BookingResponseDto
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class BookingRemoteDataSource(
  private val baseUrl: String,
  private val httpClient: HttpClient,
  private val dataStoreRepository: DataStoreRepository,
) {

  suspend fun saveBooking(bookingDto: BookingDto): BookingResponseDto {
    val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
    val response =
      httpClient.post(baseUrl.plus(BOOKING_ENDPOINT)) {
        contentType(ContentType.Application.Json)
        setBody(bookingDto)
        headers { append("Authorization", "Bearer $token") }
      }

    return response.body()
  }

  suspend fun findBookingsByUserId(userId: String): List<BookingResponseDto> {
    val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
    val url = baseUrl.plus(BOOKING_ENDPOINT).plus("/$userId")
    val response = httpClient.get(url) { headers { append("Authorization", "Bearer $token") } }

    return response.body()
  }

  companion object {
    const val BOOKING_ENDPOINT = "/api/v1/booking"
  }
}
