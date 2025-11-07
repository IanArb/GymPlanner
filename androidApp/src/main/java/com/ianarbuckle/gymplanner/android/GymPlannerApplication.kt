package com.ianarbuckle.gymplanner.android

import android.app.Application
import com.ianarbuckle.gymplanner.android.BuildConfig.BASE_URL
import com.ianarbuckle.gymplanner.android.BuildConfig.WEBSOCKET_URL
import com.ianarbuckle.gymplanner.di.initKoin
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext

@HiltAndroidApp
class GymPlannerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(baseUrl = BASE_URL, websocketBaseUrl = WEBSOCKET_URL, enableNetworkLogs = true) {
            androidContext(this@GymPlannerApplication)
        }
    }
}
