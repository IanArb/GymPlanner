package com.ianarbuckle.gymplanner.api

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.di.initKoin
import org.koin.java.KoinJavaComponent

actual object GymPlannerFactory {

    actual fun create(baseUrl: String, dataStore: DataStore<Preferences>): GymPlanner {
//        initKoin(baseUrl = baseUrl, dataStore = dataStore)
        return KoinJavaComponent.get(GymPlanner::class.java)
    }
}