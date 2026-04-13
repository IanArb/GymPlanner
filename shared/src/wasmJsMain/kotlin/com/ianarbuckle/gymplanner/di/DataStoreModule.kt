package com.ianarbuckle.gymplanner.di

import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.WebLocalStorageDataStoreRepository
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun dataStoreModule(): Module = module {
    single<DataStoreRepository> { WebLocalStorageDataStoreRepository() }
}
