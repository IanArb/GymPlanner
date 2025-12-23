package com.ianarbuckle.gymplanner.profile

import com.ianarbuckle.gymplanner.profile.ProfileTestDataProvider.Emails
import com.ianarbuckle.gymplanner.profile.ProfileTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.profile.ProfileTestDataProvider.ProfileDtos
import com.ianarbuckle.gymplanner.profile.ProfileTestDataProvider.Profiles
import com.ianarbuckle.gymplanner.profile.ProfileTestDataProvider.UserIds
import com.ianarbuckle.gymplanner.profile.ProfileTestDataProvider.Usernames
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class ProfileRepositoryTest {

    private lateinit var repository: FakeProfileRepository
    private lateinit var fakeRemoteDataSource: FakeProfileRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeProfileRemoteDataSource()
        repository = FakeProfileRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    // ========== Get Profile Tests ==========

    @Test
    fun `getProfile with valid user ID returns success with profile`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.john

        // When
        val result = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(Profiles.john, result.getOrNull())
        assertEquals(1, fakeRemoteDataSource.fetchProfileCalls.size)
        assertEquals(UserIds.user1, fakeRemoteDataSource.fetchProfileCalls[0])
    }

    @Test
    fun `getProfile calls remote data source with correct user ID`() = runTest {
        // When
        repository.getProfile(UserIds.user2)

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchProfileCalls.size)
        assertEquals(UserIds.user2, fakeRemoteDataSource.fetchProfileCalls[0])
    }

    @Test
    fun `getProfile maps DTO to domain model correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.jane

        // When
        val result = repository.getProfile(UserIds.user2)

        // Then
        val profile = result.getOrNull()
        assertNotNull(profile)
        assertEquals(UserIds.user2, profile.userId)
        assertEquals(Usernames.janeSmith, profile.username)
        assertEquals("Jane", profile.firstName)
        assertEquals("Smith", profile.surname)
        assertEquals(Emails.jane, profile.email)
    }

    @Test
    fun `getProfile with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.networkError

        // When
        val result = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getProfile with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.serverError

        // When
        val result = repository.getProfile(UserIds.user2)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `getProfile with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.unauthorized

        // When
        val result = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    @Test
    fun `getProfile with not found error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.notFound

        // When
        val result = repository.getProfile(UserIds.nonExistent)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.notFound, result.exceptionOrNull())
    }

    @Test
    fun `getProfile with timeout error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.timeout

        // When
        val result = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `getProfile with different users works independently`() = runTest {
        // When
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.john
        val result1 = repository.getProfile(UserIds.user1)

        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.jane
        val result2 = repository.getProfile(UserIds.user2)

        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.mike
        val result3 = repository.getProfile(UserIds.user3)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(Profiles.john, result1.getOrNull())
        assertEquals(Profiles.jane, result2.getOrNull())
        assertEquals(Profiles.mike, result3.getOrNull())
        assertEquals(3, fakeRemoteDataSource.fetchProfileCalls.size)
    }

    @Test
    fun `multiple getProfile calls work independently`() = runTest {
        // When
        val result1 = repository.getProfile(UserIds.user1)
        val result2 = repository.getProfile(UserIds.user2)
        val result3 = repository.getProfile(UserIds.user3)
        val result4 = repository.getProfile(UserIds.user4)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertTrue(result4.isSuccess)
        assertEquals(4, fakeRemoteDataSource.fetchProfileCalls.size)
        assertEquals(UserIds.user1, fakeRemoteDataSource.fetchProfileCalls[0])
        assertEquals(UserIds.user2, fakeRemoteDataSource.fetchProfileCalls[1])
        assertEquals(UserIds.user3, fakeRemoteDataSource.fetchProfileCalls[2])
        assertEquals(UserIds.user4, fakeRemoteDataSource.fetchProfileCalls[3])
    }

    @Test
    fun `getProfile response contains all profile fields`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.sarah

        // When
        val result = repository.getProfile(UserIds.user4)

        // Then
        val profile = result.getOrNull()
        assertNotNull(profile)
        assertNotNull(profile.userId)
        assertNotNull(profile.username)
        assertNotNull(profile.firstName)
        assertNotNull(profile.surname)
        assertNotNull(profile.email)
    }

    @Test
    fun `getProfile preserves all field values`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.mike

        // When
        val result = repository.getProfile(UserIds.user3)

        // Then
        val profile = result.getOrNull()
        assertNotNull(profile)
        assertEquals(UserIds.user3, profile.userId)
        assertEquals(Usernames.mikeBrown, profile.username)
        assertEquals("Mike", profile.firstName)
        assertEquals("Brown", profile.surname)
        assertEquals(Emails.mike, profile.email)
    }

    @Test
    fun `getProfile with admin user is handled`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.admin

        // When
        val result = repository.getProfile(UserIds.admin)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(Profiles.admin, result.getOrNull())
        assertEquals("Admin", result.getOrNull()?.firstName)
    }

    // ========== Exception Handling Tests ==========

    @Test
    fun `getProfile handles generic exceptions gracefully`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.invalidUserId

        // When
        val result = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `successful getProfile does not throw exceptions`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getProfile(UserIds.user1)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }

    // ========== Edge Cases ==========

    @Test
    fun `getProfile with empty user ID is handled`() = runTest {
        // When
        val result = repository.getProfile(UserIds.emptyId)

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchProfileCalls.size)
        assertEquals("", fakeRemoteDataSource.fetchProfileCalls[0])
    }

    @Test
    fun `getProfile with non-existent user ID is handled`() = runTest {
        // When
        val result = repository.getProfile(UserIds.nonExistent)

        // Then
        assertEquals(1, fakeRemoteDataSource.fetchProfileCalls.size)
        assertEquals(UserIds.nonExistent, fakeRemoteDataSource.fetchProfileCalls[0])
    }

    @Test
    fun `getProfile email field is preserved correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.john

        // When
        val result = repository.getProfile(UserIds.user1)

        // Then
        val profile = result.getOrNull()
        assertNotNull(profile)
        assertEquals(Emails.john, profile.email)
        assertTrue(profile.email.contains("@"))
        assertTrue(profile.email.contains(".com"))
    }

    @Test
    fun `getProfile username field is preserved correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.jane

        // When
        val result = repository.getProfile(UserIds.user2)

        // Then
        val profile = result.getOrNull()
        assertNotNull(profile)
        assertEquals(Usernames.janeSmith, profile.username)
        assertTrue(profile.username.isNotEmpty())
    }

    @Test
    fun `getProfile full name fields are preserved correctly`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.mike

        // When
        val result = repository.getProfile(UserIds.user3)

        // Then
        val profile = result.getOrNull()
        assertNotNull(profile)
        assertEquals("Mike", profile.firstName)
        assertEquals("Brown", profile.surname)
        // Full name can be constructed
        val fullName = "${profile.firstName} ${profile.surname}"
        assertEquals("Mike Brown", fullName)
    }

    @Test
    fun `getProfile with same user ID multiple times returns consistent results`() = runTest {
        // Given
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.sarah

        // When
        val result1 = repository.getProfile(UserIds.user4)
        val result2 = repository.getProfile(UserIds.user4)
        val result3 = repository.getProfile(UserIds.user4)

        // Then
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(result1.getOrNull(), result2.getOrNull())
        assertEquals(result2.getOrNull(), result3.getOrNull())
        assertEquals(3, fakeRemoteDataSource.fetchProfileCalls.size)
        // All calls with same user ID
        assertTrue(fakeRemoteDataSource.fetchProfileCalls.all { it == UserIds.user4 })
    }

    @Test
    fun `getProfile after error can retry successfully`() = runTest {
        // Given - First attempt fails
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = true
        fakeRemoteDataSource.fetchProfileException = Exceptions.networkError

        // When - First attempt
        val result1 = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result1.isFailure)

        // Given - Second attempt succeeds
        fakeRemoteDataSource.shouldThrowExceptionOnFetchProfile = false
        fakeRemoteDataSource.fetchProfileResponse = ProfileDtos.john

        // When - Retry
        val result2 = repository.getProfile(UserIds.user1)

        // Then
        assertTrue(result2.isSuccess)
        assertEquals(Profiles.john, result2.getOrNull())
        assertEquals(2, fakeRemoteDataSource.fetchProfileCalls.size)
    }
}
