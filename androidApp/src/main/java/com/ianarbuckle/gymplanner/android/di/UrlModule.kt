package com.ianarbuckle.gymplanner.android.di

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
        return BASE_URL
    }

    companion object {
        const val BASE_URL = "https://24c4-86-45-28-173.ngrok-free.app"
        const val NAMED_BASE_URL = "BASE_URL"
    }
}
