package com.ianarbuckle.gymplanner.android.dashboard.fakes

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ianarbuckle.gymplanner.storage.DataStoreRepository

class FakeDataStoreRepository : DataStoreRepository {

  override suspend fun saveData(key: Preferences.Key<String>, value: String) {
    // noop
  }

  override suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
    // noop
  }

  override suspend fun getStringData(key: Preferences.Key<String>): String? {
    return when (key) {
      stringPreferencesKey("user_id") -> return "123"
      stringPreferencesKey("auth_token") -> return "token"
      else -> null
    }
  }

  override suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? {
    return when (key) {
      stringPreferencesKey("remember_me") -> return false
      else -> null
    }
  }
}
