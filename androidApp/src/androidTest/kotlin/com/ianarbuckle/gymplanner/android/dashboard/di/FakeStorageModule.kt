package com.ianarbuckle.gymplanner.android.dashboard.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.android.dashboard.fakes.FakeDataStoreRepository
import com.ianarbuckle.gymplanner.android.di.StorageModule
import com.ianarbuckle.gymplanner.android.utils.FakeDataStore
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [StorageModule::class]
)
@Module
class FakeStorageModule {

    @Provides
    fun providesDataStoreRepository(): DataStoreRepository {
        return FakeDataStoreRepository()
    }

    @Provides
    fun providesDataStore(): DataStore<Preferences> {
        return FakeDataStore()
    }

}