package com.ianarbuckle.gymplanner.storage

import androidx.datastore.preferences.core.Preferences

/**
 * Fake implementation for testing DataStoreRepository
 * Implements the DataStoreRepository interface
 */
class FakeDataStoreRepository : DataStoreRepository {

    // In-memory storage for testing
    private val stringStorage = mutableMapOf<Preferences.Key<String>, String>()
    private val booleanStorage = mutableMapOf<Preferences.Key<Boolean>, Boolean>()

    // Control flags for test scenarios
    var shouldThrowExceptionOnSaveString = false
    var shouldThrowExceptionOnSaveBoolean = false
    var shouldThrowExceptionOnGetString = false
    var shouldThrowExceptionOnGetBoolean = false
    var saveStringException: Exception? = null
    var saveBooleanException: Exception? = null
    var getStringException: Exception? = null
    var getBooleanException: Exception? = null

    // Captured calls for verification
    val saveStringCalls = mutableListOf<Pair<Preferences.Key<String>, String>>()
    val saveBooleanCalls = mutableListOf<Pair<Preferences.Key<Boolean>, Boolean>>()
    val getStringCalls = mutableListOf<Preferences.Key<String>>()
    val getBooleanCalls = mutableListOf<Preferences.Key<Boolean>>()

    override suspend fun saveData(key: Preferences.Key<String>, value: String) {
        saveStringCalls.add(Pair(key, value))

        if (shouldThrowExceptionOnSaveString) {
            throw saveStringException ?: RuntimeException("Save string failed")
        }

        stringStorage[key] = value
    }

    override suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
        saveBooleanCalls.add(Pair(key, value))

        if (shouldThrowExceptionOnSaveBoolean) {
            throw saveBooleanException ?: RuntimeException("Save boolean failed")
        }

        booleanStorage[key] = value
    }

    override suspend fun getStringData(key: Preferences.Key<String>): String? {
        getStringCalls.add(key)

        if (shouldThrowExceptionOnGetString) {
            throw getStringException ?: RuntimeException("Get string failed")
        }

        return stringStorage[key]
    }

    override suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? {
        getBooleanCalls.add(key)

        if (shouldThrowExceptionOnGetBoolean) {
            throw getBooleanException ?: RuntimeException("Get boolean failed")
        }

        return booleanStorage[key]
    }

    fun reset() {
        stringStorage.clear()
        booleanStorage.clear()
        shouldThrowExceptionOnSaveString = false
        shouldThrowExceptionOnSaveBoolean = false
        shouldThrowExceptionOnGetString = false
        shouldThrowExceptionOnGetBoolean = false
        saveStringException = null
        saveBooleanException = null
        getStringException = null
        getBooleanException = null
        saveStringCalls.clear()
        saveBooleanCalls.clear()
        getStringCalls.clear()
        getBooleanCalls.clear()
    }
}

