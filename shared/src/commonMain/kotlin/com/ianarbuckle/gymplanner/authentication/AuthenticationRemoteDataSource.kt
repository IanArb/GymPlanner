package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.dto.LoginResponseDto
import com.ianarbuckle.gymplanner.authentication.dto.RegisterResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class AuthenticationRemoteDataSource(
  private val baseurl: String,
  private val httpClient: HttpClient,
) {

  suspend fun login(login: Login): LoginResponseDto {
    val response =
      httpClient.post(baseurl.plus(LOGIN_ENDPOINT)) {
        contentType(ContentType.Application.Json)
        setBody(login)
      }

    return response.body()
  }

  suspend fun register(register: Register): RegisterResponseDto {
    val response =
      httpClient.post(baseurl.plus(REGISTER_ENDPOINT)) {
        contentType(ContentType.Application.Json)
        setBody(register)
      }

    return response.body()
  }

  companion object {
    private const val ENDPOINT = "/api/v1/auth"
    private const val LOGIN_ENDPOINT = "$ENDPOINT/login"
    private const val REGISTER_ENDPOINT = "$ENDPOINT/register"
  }
}
