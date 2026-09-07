package com.ianarbuckle.gymplanner.android

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.ianarbuckle.gymplanner.android.BuildConfig.BASE_URL
import com.ianarbuckle.gymplanner.android.BuildConfig.WEBSOCKET_URL
import com.ianarbuckle.gymplanner.android.imageloading.GymPlannerImageLoaderFactory
import com.ianarbuckle.gymplanner.di.initKoin
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext

@HiltAndroidApp
class GymPlannerApplication : BaseApplication(), SingletonImageLoader.Factory {

    private val imageLoaderFactory = GymPlannerImageLoaderFactory()

    override fun onCreate() {
        super.onCreate()
        initKoin(baseUrl = BASE_URL, websocketBaseUrl = WEBSOCKET_URL, enableNetworkLogs = true) {
            androidContext(this@GymPlannerApplication)
        }
    }

    override fun newImageLoader(context: Context): ImageLoader =
        imageLoaderFactory.newImageLoader(context)
}
