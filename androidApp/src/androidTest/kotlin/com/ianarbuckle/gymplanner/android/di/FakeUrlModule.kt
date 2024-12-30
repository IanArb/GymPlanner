package com.ianarbuckle.gymplanner.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UrlModule::class],
)
class FakeUrlModule {

    @Provides
    @Named(UrlModule.NAMED_BASE_URL)
    @Singleton
    fun provideUrl(): String = "http://localhost:8080/"
}
