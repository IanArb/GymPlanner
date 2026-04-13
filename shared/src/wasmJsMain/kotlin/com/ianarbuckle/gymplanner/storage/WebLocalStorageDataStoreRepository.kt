package com.ianarbuckle.gymplanner.storage

import androidx.datastore.preferences.core.Preferences

private external val localStorage: JsLocalStorage

private external interface JsLocalStorage {
    fun getItem(key: String): String?

    fun setItem(key: String, value: String)

    fun clear()
}

class WebLocalStorageDataStoreRepository : DataStoreRepository {

    override suspend fun saveData(key: Preferences.Key<String>, value: String) {
        localStorage.setItem(key.name, value)
    }

    override suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
        localStorage.setItem(key.name, value.toString())
    }

    override suspend fun getStringData(key: Preferences.Key<String>): String? =
        localStorage.getItem(key.name)

    override suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? =
        localStorage.getItem(key.name)?.toBoolean()

    override suspend fun clearAllData() {
        localStorage.clear()
    }
}
