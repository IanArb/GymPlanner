package com.ianarbuckle.gymplanner.storage

import com.ianarbuckle.gymplanner.storage.DataStoreTestDataProvider.BooleanKeys
import com.ianarbuckle.gymplanner.storage.DataStoreTestDataProvider.BooleanValues
import com.ianarbuckle.gymplanner.storage.DataStoreTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.storage.DataStoreTestDataProvider.StringKeys
import com.ianarbuckle.gymplanner.storage.DataStoreTestDataProvider.StringValues
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DataStoreRepositoryTest {

    private lateinit var repository: FakeDataStoreRepository

    @BeforeTest
    fun setup() {
        repository = FakeDataStoreRepository()
    }

    @AfterTest
    fun tearDown() {
        repository.reset()
    }

    // ========== Save String Data Tests ==========

    @Test
    fun `saveData string with auth token stores value successfully`() = runTest {
        // When
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)

        // Then
        assertEquals(1, repository.saveStringCalls.size)
        assertEquals(StringKeys.authToken, repository.saveStringCalls[0].first)
        assertEquals(StringValues.authTokenValue, repository.saveStringCalls[0].second)
        // Verify it was actually saved
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
    }

    @Test
    fun `saveData string with user ID stores value successfully`() = runTest {
        // When
        repository.saveData(StringKeys.userId, StringValues.userIdValue)

        // Then
        assertEquals(StringValues.userIdValue, repository.getStringData(StringKeys.userId))
    }

    @Test
    fun `saveData string with multiple keys works independently`() = runTest {
        // When
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(StringKeys.userId, StringValues.userIdValue)
        repository.saveData(StringKeys.username, StringValues.usernameValue)

        // Then
        assertEquals(3, repository.saveStringCalls.size)
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(StringValues.userIdValue, repository.getStringData(StringKeys.userId))
        assertEquals(StringValues.usernameValue, repository.getStringData(StringKeys.username))
    }

    @Test
    fun `saveData string overwrites existing value`() = runTest {
        // Given - Save initial value
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)

        // When - Overwrite with new value
        repository.saveData(StringKeys.authToken, StringValues.updatedTokenValue)

        // Then
        assertEquals(2, repository.saveStringCalls.size)
        assertEquals(StringValues.updatedTokenValue, repository.getStringData(StringKeys.authToken))
    }

    @Test
    fun `saveData string with empty value is handled`() = runTest {
        // When
        repository.saveData(StringKeys.username, StringValues.emptyValue)

        // Then
        assertEquals(StringValues.emptyValue, repository.getStringData(StringKeys.username))
    }

    @Test
    fun `saveData string with exception throws error`() = runTest {
        // Given
        repository.shouldThrowExceptionOnSaveString = true
        repository.saveStringException = Exceptions.storageError

        // When & Then
        var exception: Exception? = null
        try {
            repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        } catch (e: Exception) {
            exception = e
        }

        assertEquals(Exceptions.storageError, exception)
    }

    // ========== Get String Data Tests ==========

    @Test
    fun `getStringData returns saved value`() = runTest {
        // Given
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)

        // When
        val result = repository.getStringData(StringKeys.authToken)

        // Then
        assertEquals(StringValues.authTokenValue, result)
        assertEquals(1, repository.getStringCalls.size)
        assertEquals(StringKeys.authToken, repository.getStringCalls[0])
    }

    @Test
    fun `getStringData returns null for non-existent key`() = runTest {
        // When
        val result = repository.getStringData(StringKeys.authToken)

        // Then
        assertNull(result)
    }

    @Test
    fun `getStringData with multiple keys works independently`() = runTest {
        // Given
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(StringKeys.userId, StringValues.userIdValue)
        repository.saveData(StringKeys.email, StringValues.emailValue)

        // When
        val token = repository.getStringData(StringKeys.authToken)
        val userId = repository.getStringData(StringKeys.userId)
        val email = repository.getStringData(StringKeys.email)

        // Then
        assertEquals(StringValues.authTokenValue, token)
        assertEquals(StringValues.userIdValue, userId)
        assertEquals(StringValues.emailValue, email)
        assertEquals(3, repository.getStringCalls.size)
    }

    @Test
    fun `getStringData with exception throws error`() = runTest {
        // Given
        repository.shouldThrowExceptionOnGetString = true
        repository.getStringException = Exceptions.corruptedData

        // When & Then
        var exception: Exception? = null
        try {
            repository.getStringData(StringKeys.authToken)
        } catch (e: Exception) {
            exception = e
        }

        assertEquals(Exceptions.corruptedData, exception)
    }

    // ========== Save Boolean Data Tests ==========

    @Test
    fun `saveData boolean with remember me stores value successfully`() = runTest {
        // When
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)

        // Then
        assertEquals(1, repository.saveBooleanCalls.size)
        assertEquals(BooleanKeys.rememberMe, repository.saveBooleanCalls[0].first)
        assertEquals(BooleanValues.trueValue, repository.saveBooleanCalls[0].second)
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.rememberMe))
    }

    @Test
    fun `saveData boolean with false value is handled`() = runTest {
        // When
        repository.saveData(BooleanKeys.notificationsEnabled, BooleanValues.falseValue)

        // Then
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.notificationsEnabled))
    }

    @Test
    fun `saveData boolean with multiple keys works independently`() = runTest {
        // When
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.isLoggedIn, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.darkMode, BooleanValues.falseValue)

        // Then
        assertEquals(3, repository.saveBooleanCalls.size)
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.rememberMe))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.isLoggedIn))
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.darkMode))
    }

    @Test
    fun `saveData boolean overwrites existing value`() = runTest {
        // Given
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)

        // When
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.falseValue)

        // Then
        assertEquals(2, repository.saveBooleanCalls.size)
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.rememberMe))
    }

    @Test
    fun `saveData boolean with exception throws error`() = runTest {
        // Given
        repository.shouldThrowExceptionOnSaveBoolean = true
        repository.saveBooleanException = Exceptions.ioError

        // When & Then
        var exception: Exception? = null
        try {
            repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        } catch (e: Exception) {
            exception = e
        }

        assertEquals(Exceptions.ioError, exception)
    }

    // ========== Get Boolean Data Tests ==========

    @Test
    fun `getBooleanData returns saved value`() = runTest {
        // Given
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)

        // When
        val result = repository.getBooleanData(BooleanKeys.rememberMe)

        // Then
        assertEquals(BooleanValues.trueValue, result)
        assertEquals(1, repository.getBooleanCalls.size)
        assertEquals(BooleanKeys.rememberMe, repository.getBooleanCalls[0])
    }

    @Test
    fun `getBooleanData returns null for non-existent key`() = runTest {
        // When
        val result = repository.getBooleanData(BooleanKeys.rememberMe)

        // Then
        assertNull(result)
    }

    @Test
    fun `getBooleanData with multiple keys works independently`() = runTest {
        // Given
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.isLoggedIn, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.notificationsEnabled, BooleanValues.falseValue)

        // When
        val rememberMe = repository.getBooleanData(BooleanKeys.rememberMe)
        val isLoggedIn = repository.getBooleanData(BooleanKeys.isLoggedIn)
        val notifications = repository.getBooleanData(BooleanKeys.notificationsEnabled)

        // Then
        assertEquals(BooleanValues.trueValue, rememberMe)
        assertEquals(BooleanValues.trueValue, isLoggedIn)
        assertEquals(BooleanValues.falseValue, notifications)
        assertEquals(3, repository.getBooleanCalls.size)
    }

    @Test
    fun `getBooleanData with exception throws error`() = runTest {
        // Given
        repository.shouldThrowExceptionOnGetBoolean = true
        repository.getBooleanException = Exceptions.storageError

        // When & Then
        var exception: Exception? = null
        try {
            repository.getBooleanData(BooleanKeys.rememberMe)
        } catch (e: Exception) {
            exception = e
        }

        assertEquals(Exceptions.storageError, exception)
    }

    // ========== Integration Tests ==========

    @Test
    fun `save and get string data workflow works correctly`() = runTest {
        // When - Save
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(StringKeys.userId, StringValues.userIdValue)

        // Then - Get
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(StringValues.userIdValue, repository.getStringData(StringKeys.userId))
    }

    @Test
    fun `save and get boolean data workflow works correctly`() = runTest {
        // When - Save
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.darkMode, BooleanValues.falseValue)

        // Then - Get
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.rememberMe))
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.darkMode))
    }

    @Test
    fun `mixed string and boolean operations work correctly`() = runTest {
        // When
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        repository.saveData(StringKeys.userId, StringValues.userIdValue)
        repository.saveData(BooleanKeys.isLoggedIn, BooleanValues.trueValue)

        // Then
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.rememberMe))
        assertEquals(StringValues.userIdValue, repository.getStringData(StringKeys.userId))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.isLoggedIn))
    }

    // ========== Edge Cases ==========

    @Test
    fun `updating same key multiple times works correctly`() = runTest {
        // When
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))

        repository.saveData(StringKeys.authToken, StringValues.updatedTokenValue)
        assertEquals(StringValues.updatedTokenValue, repository.getStringData(StringKeys.authToken))

        repository.saveData(StringKeys.authToken, StringValues.emptyValue)
        assertEquals(StringValues.emptyValue, repository.getStringData(StringKeys.authToken))

        // Then
        assertEquals(3, repository.saveStringCalls.size)
    }

    @Test
    fun `toggling boolean value multiple times works correctly`() = runTest {
        // When
        repository.saveData(BooleanKeys.darkMode, BooleanValues.trueValue)
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.darkMode))

        repository.saveData(BooleanKeys.darkMode, BooleanValues.falseValue)
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.darkMode))

        repository.saveData(BooleanKeys.darkMode, BooleanValues.trueValue)
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.darkMode))

        // Then
        assertEquals(3, repository.saveBooleanCalls.size)
    }

    @Test
    fun `different keys with same value type are independent`() = runTest {
        // When - Save different keys with same values
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(StringKeys.refreshToken, StringValues.authTokenValue) // Same value, different key

        // Then - Each key maintains its own value
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.refreshToken))

        // Update one shouldn't affect the other
        repository.saveData(StringKeys.authToken, StringValues.updatedTokenValue)
        assertEquals(StringValues.updatedTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.refreshToken))
    }

    @Test
    fun `getStringData before any save returns null`() = runTest {
        // When
        val token = repository.getStringData(StringKeys.authToken)
        val userId = repository.getStringData(StringKeys.userId)
        val email = repository.getStringData(StringKeys.email)

        // Then
        assertNull(token)
        assertNull(userId)
        assertNull(email)
    }

    @Test
    fun `getBooleanData before any save returns null`() = runTest {
        // When
        val rememberMe = repository.getBooleanData(BooleanKeys.rememberMe)
        val darkMode = repository.getBooleanData(BooleanKeys.darkMode)
        val notifications = repository.getBooleanData(BooleanKeys.notificationsEnabled)

        // Then
        assertNull(rememberMe)
        assertNull(darkMode)
        assertNull(notifications)
    }

    @Test
    fun `authentication workflow simulation works correctly`() = runTest {
        // Simulate login - save auth data
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(StringKeys.userId, StringValues.userIdValue)
        repository.saveData(StringKeys.username, StringValues.usernameValue)
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.isLoggedIn, BooleanValues.trueValue)

        // Verify all data is saved
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(StringValues.userIdValue, repository.getStringData(StringKeys.userId))
        assertEquals(StringValues.usernameValue, repository.getStringData(StringKeys.username))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.rememberMe))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.isLoggedIn))

        // Verify call counts
        assertEquals(3, repository.saveStringCalls.size)
        assertEquals(2, repository.saveBooleanCalls.size)
    }

    @Test
    fun `all string keys are independent`() = runTest {
        // When - Save to all string keys
        repository.saveData(StringKeys.authToken, StringValues.authTokenValue)
        repository.saveData(StringKeys.userId, StringValues.userIdValue)
        repository.saveData(StringKeys.username, StringValues.usernameValue)
        repository.saveData(StringKeys.email, StringValues.emailValue)
        repository.saveData(StringKeys.refreshToken, StringValues.refreshTokenValue)
        repository.saveData(StringKeys.deviceId, StringValues.deviceIdValue)

        // Then - All values are correctly stored
        assertEquals(6, repository.saveStringCalls.size)
        assertEquals(StringValues.authTokenValue, repository.getStringData(StringKeys.authToken))
        assertEquals(StringValues.userIdValue, repository.getStringData(StringKeys.userId))
        assertEquals(StringValues.usernameValue, repository.getStringData(StringKeys.username))
        assertEquals(StringValues.emailValue, repository.getStringData(StringKeys.email))
        assertEquals(StringValues.refreshTokenValue, repository.getStringData(StringKeys.refreshToken))
        assertEquals(StringValues.deviceIdValue, repository.getStringData(StringKeys.deviceId))
    }

    @Test
    fun `all boolean keys are independent`() = runTest {
        // When - Save to all boolean keys
        repository.saveData(BooleanKeys.rememberMe, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.isLoggedIn, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.notificationsEnabled, BooleanValues.falseValue)
        repository.saveData(BooleanKeys.darkMode, BooleanValues.trueValue)
        repository.saveData(BooleanKeys.firstLaunch, BooleanValues.falseValue)

        // Then - All values are correctly stored
        assertEquals(5, repository.saveBooleanCalls.size)
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.rememberMe))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.isLoggedIn))
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.notificationsEnabled))
        assertEquals(BooleanValues.trueValue, repository.getBooleanData(BooleanKeys.darkMode))
        assertEquals(BooleanValues.falseValue, repository.getBooleanData(BooleanKeys.firstLaunch))
    }
}

