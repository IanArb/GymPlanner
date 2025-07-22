package com.ianarbuckle.gymplanner.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createDataStore(context: Context): DataStore<Preferences> = createDataStore {
    context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
}
