package com.ianarbuckle.gymplanner.android.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeDataStore : DataStore<Preferences> {

    override val data: Flow<Preferences>
        get() = flowOf(emptyPreferences())

    override suspend fun updateData(
        transform: suspend (t: Preferences) -> Preferences
    ): Preferences {
        return emptyPreferences()
    }
}
