package com.ianarbuckle.gymplanner.storage

import androidx.datastore.preferences.core.Preferences

private external val sessionStorage: JsSessionStorage

private external interface JsSessionStorage {
    fun getItem(key: String): String?

    fun setItem(key: String, value: String)

    fun clear()
}

class WebSessionStorageDataStoreRepository : DataStoreRepository {

    override suspend fun saveData(key: Preferences.Key<String>, value: String) {
        sessionStorage.setItem(key.name, value)
    }

    override suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
        sessionStorage.setItem(key.name, value.toString())
    }

    override suspend fun getStringData(key: Preferences.Key<String>): String? =
        sessionStorage.getItem(key.name)

    override suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? =
        sessionStorage.getItem(key.name)?.toBoolean()

    override suspend fun clearAllData() {
        sessionStorage.clear()
    }
}
