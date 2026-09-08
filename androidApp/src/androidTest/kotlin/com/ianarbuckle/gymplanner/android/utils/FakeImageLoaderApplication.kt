package com.ianarbuckle.gymplanner.android.utils

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import com.ianarbuckle.gymplanner.android.BaseApplication
import com.ianarbuckle.gymplanner.di.initKoin

open class FakeImageLoaderApplication :
    BaseApplication(),
    SingletonImageLoader.Factory {
    private val imageLoaderFactory = FakeImageLoaderFactory()

    override fun onCreate() {
        super.onCreate()
        initKoin(baseUrl = "http://localhost:8080/", websocketBaseUrl = "wss://localhost:8000/")
    }

    override fun newImageLoader(context: Context): ImageLoader = imageLoaderFactory.newImageLoader(context)
}
