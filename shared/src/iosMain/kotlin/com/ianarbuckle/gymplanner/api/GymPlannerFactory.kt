package com.ianarbuckle.gymplanner.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual object GymPlannerFactory {

    actual fun create(baseUrl: String, dataStore: DataStore<Preferences>): GymPlanner {
        TODO("Not yet implemented")
    }
}
