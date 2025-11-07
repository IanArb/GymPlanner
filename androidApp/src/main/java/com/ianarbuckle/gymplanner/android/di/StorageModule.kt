package com.ianarbuckle.gymplanner.android.di

import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.DefaultDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    @Singleton
    fun providesDataStoreRepository(): DataStoreRepository {
        return DefaultDataStoreRepository()
    }
}
