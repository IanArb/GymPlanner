package com.ianarbuckle.gymplanner.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
val REMEMBER_ME_KEY = booleanPreferencesKey("remember_me")
val USER_ID = stringPreferencesKey("user_id")

interface DataStoreRepository {
    suspend fun saveData(key: Preferences.Key<String>, value: String)
    suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean)
    suspend fun getStringData(key: Preferences.Key<String>): String?
    suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean?
}

class DefaultDataStoreRepository : DataStoreRepository, KoinComponent {

    private val dataStore: DataStore<Preferences> by inject()

    override suspend fun saveData(key: Preferences.Key<String>, value: String) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override suspend fun getStringData(key: Preferences.Key<String>): String? {
        val preferences = dataStore.data.first()
        return preferences[key]
    }

    override suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? {
        val preferences = dataStore.data.first()
        return preferences[key]
    }
}
