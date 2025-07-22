package com.ianarbuckle.gymplanner.android

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.android.di.UrlModule
import com.ianarbuckle.gymplanner.di.initKoin
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class GymPlannerApplication : BaseApplication() {

    @Inject lateinit var dataStore: DataStore<Preferences>

    @Inject @Named(UrlModule.NAMED_BASE_URL) lateinit var baseUrl: String

    @Inject @Named(UrlModule.NAMED_WEBSOCKET_URL) lateinit var websocketBaseUrl: String

    override fun onCreate() {
        super.onCreate()
        initKoin(baseUrl = baseUrl, dataStore = dataStore, websocketBaseUrl = websocketBaseUrl)
    }
}
