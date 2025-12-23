package com.ianarbuckle.gymplanner.storage

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/** Provides test data for DataStoreRepository tests */
object DataStoreTestDataProvider {

    // ========== String Keys ==========

    object StringKeys {
        val authToken = stringPreferencesKey("auth_token")
        val userId = stringPreferencesKey("user_id")
        val username = stringPreferencesKey("username")
        val email = stringPreferencesKey("email")
        val refreshToken = stringPreferencesKey("refresh_token")
        val deviceId = stringPreferencesKey("device_id")
    }

    // ========== Boolean Keys ==========

    object BooleanKeys {
        val rememberMe = booleanPreferencesKey("remember_me")
        val isLoggedIn = booleanPreferencesKey("is_logged_in")
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val darkMode = booleanPreferencesKey("dark_mode")
        val firstLaunch = booleanPreferencesKey("first_launch")
    }

    // ========== String Values ==========

    object StringValues {
        const val authTokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token"
        const val userIdValue = "user-12345"
        const val usernameValue = "johndoe"
        const val emailValue = "john.doe@example.com"
        const val refreshTokenValue = "refresh_token_abc123xyz789"
        const val deviceIdValue = "device-uuid-1234-5678"
        const val emptyValue = ""
        const val updatedTokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.updated.token"
    }

    // ========== Boolean Values ==========

    object BooleanValues {
        const val trueValue = true
        const val falseValue = false
    }

    // ========== Exceptions ==========

    object Exceptions {
        val storageError = RuntimeException("Storage operation failed")
        val corruptedData = RuntimeException("Data store corrupted")
        val ioError = RuntimeException("IO error while accessing data store")
    }
}
