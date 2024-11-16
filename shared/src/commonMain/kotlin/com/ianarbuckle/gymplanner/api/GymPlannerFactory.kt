package com.ianarbuckle.gymplanner.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences


expect object GymPlannerFactory {

    fun create(baseUrl: String, dataStore: DataStore<Preferences>): GymPlanner
}