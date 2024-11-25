package com.ianarbuckle.gymplanner.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
val USER_ID = stringPreferencesKey("user_id")

class DataStoreRepository(private val dataStore: DataStore<Preferences>) {

    suspend fun saveData(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun getStringData(key: Preferences.Key<String>): String? {
        val preferences = dataStore.data.first()
        return preferences[key]
    }

    suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? {
        val preferences = dataStore.data.first()
        return preferences[key]
    }
}