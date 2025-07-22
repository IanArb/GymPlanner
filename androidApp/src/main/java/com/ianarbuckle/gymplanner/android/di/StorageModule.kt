package com.ianarbuckle.gymplanner.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.DefaultDataStoreRepository
import com.ianarbuckle.gymplanner.storage.createDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StorageModule {

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        createDataStore(context)

    @Provides
    fun providesDataStoreRepository(): DataStoreRepository {
        return DefaultDataStoreRepository()
    }
}
