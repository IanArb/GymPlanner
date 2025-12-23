package com.ianarbuckle.gymplanner.authentication

import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.LoginDomainResponses
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.LoginResponses
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.Logins
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.RegisterDomainResponses
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.RegisterResponses
import com.ianarbuckle.gymplanner.authentication.AuthenticationTestDataProvider.Registers
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class AuthenticationRepositoryTest {

    private lateinit var repository: AuthenticationRepository
    private lateinit var fakeRemoteDataSource: FakeAuthenticationRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeAuthenticationRemoteDataSource()
        repository = FakeAuthenticationRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Login Tests ==========

    @Test
    fun `login with valid credentials returns success with login response`() = runTest {
        // Given
        fakeRemoteDataSource.loginResponse = LoginResponses.valid

        // When
        val result = repository.login(Logins.valid)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(LoginDomainResponses.valid, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.loginCalls.size)
        assertEquals(Logins.valid, fakeRemoteDataSource.loginCalls[0])
    }

    @Test
    fun `login with invalid credentials returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnLogin = true
        fakeRemoteDataSource.loginException = Exceptions.invalidCredentials

        // When
        val result = repository.login(Logins.invalid)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.invalidCredentials, result.exceptionOrNull())
        assertEquals(1, fakeRemoteDataSource.loginCalls.size)
        assertEquals(Logins.invalid, fakeRemoteDataSource.loginCalls[0])
    }

    @Test
    fun `login with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnLogin = true
        fakeRemoteDataSource.loginException = Exceptions.networkUnavailable

        // When
        val result = repository.login(Logins.valid)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkUnavailable, result.exceptionOrNull())
    }

    @Test
    fun `login maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.loginResponse = LoginResponses.mapped

        // When
        val result = repository.login(Logins.mapper)

        // Then
        assertEquals(LoginDomainResponses.mapped, result.getOrNull())
    }

    @Test
    fun `login with empty username and password still calls remote data source`() = runTest {
        // When
        val result = repository.login(Logins.empty)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(1, fakeRemoteDataSource.loginCalls.size)
        assertEquals("", fakeRemoteDataSource.loginCalls[0].username)
        assertEquals("", fakeRemoteDataSource.loginCalls[0].password)
    }

    @Test
    fun `multiple login calls maintain separate results`() = runTest {
        // When
        val result1 = repository.login(Logins.user1)

        fakeRemoteDataSource.loginResponse = LoginResponses.user2Response
        val result2 = repository.login(Logins.user2)

        // Then
        assertTrue(result1.isSuccess, "First result should be successful")
        assertTrue(result2.isSuccess, "Second result should be successful")
        assertEquals(2, fakeRemoteDataSource.loginCalls.size)
        assertEquals(Logins.user1, fakeRemoteDataSource.loginCalls[0])
        assertEquals(Logins.user2, fakeRemoteDataSource.loginCalls[1])
    }

    // ========== Register Tests ==========

    @Test
    fun `register with valid data returns success with register response`() = runTest {
        // Given
        fakeRemoteDataSource.registerResponse = RegisterResponses.success

        // When
        val result = repository.register(Registers.valid)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(RegisterDomainResponses.success, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.registerCalls.size)
        assertEquals(Registers.valid, fakeRemoteDataSource.registerCalls[0])
    }

    @Test
    fun `register with duplicate username returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegister = true
        fakeRemoteDataSource.registerException = Exceptions.usernameTaken

        // When
        val result = repository.register(Registers.duplicate)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.usernameTaken, result.exceptionOrNull())
        assertEquals(1, fakeRemoteDataSource.registerCalls.size)
    }

    @Test
    fun `register with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegister = true
        fakeRemoteDataSource.registerException = Exceptions.connectionTimeout

        // When
        val result = repository.register(Registers.networkError)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.connectionTimeout, result.exceptionOrNull())
    }

    @Test
    fun `register maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.registerResponse = RegisterResponses.customSuccess

        // When
        val result = repository.register(Registers.mapper)

        // Then
        assertEquals(RegisterDomainResponses.customSuccess, result.getOrNull())
    }

    @Test
    fun `register with all fields populated calls remote data source correctly`() = runTest {
        // When
        val result = repository.register(Registers.complete)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(1, fakeRemoteDataSource.registerCalls.size)

        val capturedRegister = fakeRemoteDataSource.registerCalls[0]
        assertEquals("completeuser", capturedRegister.username)
        assertEquals("CompletePass123!", capturedRegister.password)
        assertEquals("complete@example.com", capturedRegister.email)
        assertEquals("Complete", capturedRegister.firstName)
        assertEquals("User", capturedRegister.lastName)
    }

    @Test
    fun `register with minimum required fields still succeeds`() = runTest {
        // When
        val result = repository.register(Registers.minimal)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(1, fakeRemoteDataSource.registerCalls.size)
    }

    @Test
    fun `multiple register calls maintain separate results`() = runTest {
        // When
        val result1 = repository.register(Registers.user1)

        fakeRemoteDataSource.registerResponse = RegisterResponses.secondRegistration
        val result2 = repository.register(Registers.user2)

        // Then
        assertTrue(result1.isSuccess, "First result should be successful")
        assertTrue(result2.isSuccess, "Second result should be successful")
        assertEquals("Second registration successful", result2.getOrNull()?.message)
        assertEquals(2, fakeRemoteDataSource.registerCalls.size)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `login handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnLogin = true
        fakeRemoteDataSource.loginException = Exceptions.unexpectedError

        // When
        val result = repository.login(Logins.valid)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun `register handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnRegister = true
        fakeRemoteDataSource.registerException = Exceptions.invalidInput

        // When
        val result = repository.register(Registers.generic)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful login does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.login(Logins.valid)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    @Test
    fun `successful register does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.register(Registers.genericWithEmail)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `login with special characters in credentials is handled`() = runTest {
        // When
        val result = repository.login(Logins.withSpecialChars)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals("user@#$%", fakeRemoteDataSource.loginCalls[0].username)
        assertEquals("p@ssw0rd!#$%^&*()", fakeRemoteDataSource.loginCalls[0].password)
    }

    @Test
    fun `register with special characters in fields is handled`() = runTest {
        // When
        val result = repository.register(Registers.withSpecialChars)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals("user+test@example.com", fakeRemoteDataSource.registerCalls[0].email)
    }

    @Test
    fun `login response with zero expiration is handled`() = runTest {
        // Given
        fakeRemoteDataSource.loginResponse = LoginResponses.zeroExpiration

        // When
        val result = repository.login(Logins.valid)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(0L, result.getOrNull()?.expiration)
    }

    @Test
    fun `login response with negative expiration is handled`() = runTest {
        // Given
        fakeRemoteDataSource.loginResponse = LoginResponses.negativeExpiration

        // When
        val result = repository.login(Logins.valid)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(-1L, result.getOrNull()?.expiration)
    }
}
