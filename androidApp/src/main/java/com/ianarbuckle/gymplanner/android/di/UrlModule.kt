package com.ianarbuckle.gymplanner.android.di

import com.ianarbuckle.gymplanner.android.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class UrlModule {

    @Provides
    @Named(NAMED_BASE_URL)
    fun provideBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Named(NAMED_WEBSOCKET_URL)
    fun provideWebSocketUrl(): String {
        return BuildConfig.WEBSOCKET_URL
    }

    companion object {
        const val NAMED_BASE_URL = "BASE_URL"
        const val NAMED_WEBSOCKET_URL = "WEBSOCKET_URL"
    }
}
