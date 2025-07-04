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

    companion object {
        const val BASE_URL = "https://cc13-86-45-28-173.ngrok-free.app"
        const val NAMED_BASE_URL = "BASE_URL"
    }
}
