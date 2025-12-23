package com.ianarbuckle.gymplanner.fcm

import com.ianarbuckle.gymplanner.fcm.FcmTokenTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.fcm.FcmTokenTestDataProvider.FcmTokenRequests
import com.ianarbuckle.gymplanner.fcm.FcmTokenTestDataProvider.FcmTokenResponseDtos
import com.ianarbuckle.gymplanner.fcm.FcmTokenTestDataProvider.FcmTokenResponses
import com.ianarbuckle.gymplanner.fcm.FcmTokenTestDataProvider.FcmTokens
import com.ianarbuckle.gymplanner.fcm.FcmTokenTestDataProvider.UserIds
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FcmTokenRepositoryTest {

    private lateinit var repository: FakeFcmTokenRepository
    private lateinit var fakeRemoteDataSource: FakeFcmTokenRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeFcmTokenRemoteDataSource()
        repository = FakeFcmTokenRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Register FCM Token Tests ==========

    @Test
    fun `registerToken with valid request returns success`() = runTest {
        // Given
        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.successResponse

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(FcmTokenResponses.successResponse, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.registerTokenCalls.size)
    }

    @Test
    fun `registerToken calls remote data source with correct data`() = runTest {
        // When
        repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertEquals(1, fakeRemoteDataSource.registerTokenCalls.size)
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertEquals(UserIds.user1, capturedRequest.userId)
        assertEquals(FcmTokens.validToken1, capturedRequest.token)
    }

    @Test
    fun `registerToken maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.successResponse

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(FcmTokens.validToken1, response.token)
    }

    @Test
    fun `registerToken with different users works independently`() = runTest {
        // When
        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.successResponse
        val result1 = repository.registerToken(FcmTokenRequests.user1Request)

        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.user2Response
        val result2 = repository.registerToken(FcmTokenRequests.user2Request)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeRemoteDataSource.registerTokenCalls.size)
        assertEquals(UserIds.user1, fakeRemoteDataSource.registerTokenCalls[0].userId)
        assertEquals(UserIds.user2, fakeRemoteDataSource.registerTokenCalls[1].userId)
    }

    @Test
    fun `registerToken with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.networkError

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `registerToken with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.serverError

        // When
        val result = repository.registerToken(FcmTokenRequests.user2Request)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `registerToken with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.unauthorized

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `registerToken with registration failed error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.registrationFailed

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.registrationFailed, result.exceptionOrNull())
    }

    @Test
    fun `registerToken with timeout error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.timeout

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `multiple registerToken calls work independently`() = runTest {
        // When
        val result1 = repository.registerToken(FcmTokenRequests.user1Request)
        val result2 = repository.registerToken(FcmTokenRequests.user2Request)
        val result3 = repository.registerToken(FcmTokenRequests.user3Request)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(3, fakeRemoteDataSource.registerTokenCalls.size)
    }

    @Test
    fun `registerToken with null response token is handled`() = runTest {
        // Given
        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.nullTokenResponse

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals(null, response.token)
    }

    @Test
    fun `registerToken with empty response token is handled`() = runTest {
        // Given
        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.emptyTokenResponse

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isSuccess)
        val response = result.getOrNull()
        assertNotNull(response)
        assertEquals("", response.token)
    }

    @Test
    fun `registerToken preserves request userId`() = runTest {
        // When
        repository.registerToken(FcmTokenRequests.user2Request)

        // Then
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertEquals(UserIds.user2, capturedRequest.userId)
    }

    @Test
    fun `registerToken preserves request token`() = runTest {
        // When
        repository.registerToken(FcmTokenRequests.user3Request)

        // Then
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertEquals(FcmTokens.validToken3, capturedRequest.token)
    }

    // ========== Edge Case Tests ==========

    @Test
    fun `registerToken with empty userId is handled`() = runTest {
        // When
        val result = repository.registerToken(FcmTokenRequests.emptyUserIdRequest)

        // Then
        assertTrue(result.isSuccess)
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertEquals("", capturedRequest.userId)
    }

    @Test
    fun `registerToken with empty token is handled`() = runTest {
        // When
        val result = repository.registerToken(FcmTokenRequests.emptyTokenRequest)

        // Then
        assertTrue(result.isSuccess)
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertEquals("", capturedRequest.token)
    }

    @Test
    fun `registerToken with expired token is handled`() = runTest {
        // When
        val result = repository.registerToken(FcmTokenRequests.expiredTokenRequest)

        // Then
        assertTrue(result.isSuccess)
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertEquals(FcmTokens.expiredToken, capturedRequest.token)
    }

    @Test
    fun `registerToken with long token is handled`() = runTest {
        // When
        val result = repository.registerToken(FcmTokenRequests.longTokenRequest)

        // Then
        assertTrue(result.isSuccess)
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertTrue(capturedRequest.token.length > 100)
    }

    @Test
    fun `registerToken response contains token field`() = runTest {
        // Given
        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.successResponse

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        val response = result.getOrNull()
        assertNotNull(response)
        assertNotNull(response.token)
        assertEquals(FcmTokens.validToken1, response.token)
    }

    @Test
    fun `registerToken request contains all required fields`() = runTest {
        // When
        repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        val capturedRequest = fakeRemoteDataSource.registerTokenCalls[0]
        assertNotNull(capturedRequest.userId)
        assertNotNull(capturedRequest.token)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `registerToken handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.invalidToken

        // When
        val result = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `registerToken handles token expired exception gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.tokenExpired

        // When
        val result = repository.registerToken(FcmTokenRequests.expiredTokenRequest)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.tokenExpired, result.exceptionOrNull())
    }

    @Test
    fun `successful registerToken does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.registerToken(FcmTokenRequests.user1Request)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Token Update Scenarios ==========

    @Test
    fun `registerToken with same user different tokens works correctly`() = runTest {
        // When - User updates their FCM token
        val result1 = repository.registerToken(FcmTokenRequests.user1Request)

        fakeRemoteDataSource.registerTokenResponse = FcmTokenResponseDtos.user2Response
        val updatedRequest = FcmTokenRequests.user1Request.copy(token = FcmTokens.validToken2)
        val result2 = repository.registerToken(updatedRequest)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeRemoteDataSource.registerTokenCalls.size)
        assertEquals(UserIds.user1, fakeRemoteDataSource.registerTokenCalls[0].userId)
        assertEquals(UserIds.user1, fakeRemoteDataSource.registerTokenCalls[1].userId)
        assertEquals(FcmTokens.validToken1, fakeRemoteDataSource.registerTokenCalls[0].token)
        assertEquals(FcmTokens.validToken2, fakeRemoteDataSource.registerTokenCalls[1].token)
    }

    @Test
    fun `registerToken after failed attempt retries successfully`() = runTest {
        // Given - First attempt fails
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = true
        fakeRemoteDataSource.registerTokenException = Exceptions.networkError

        // When - First attempt
        val result1 = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result1.isFailure)

        // Given - Second attempt succeeds
        fakeRemoteDataSource.shouldThrowExceptionOnRegisterToken = false

        // When - Retry
        val result2 = repository.registerToken(FcmTokenRequests.user1Request)

        // Then
        assertTrue(result2.isSuccess)
        assertEquals(2, fakeRemoteDataSource.registerTokenCalls.size)
    }
}

