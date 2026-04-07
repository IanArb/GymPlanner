package com.ianarbuckle.gymplanner.di

import com.ianarbuckle.gymplanner.storage.DATA_STORE_FILE_NAME
import com.ianarbuckle.gymplanner.storage.createDataStore
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { createDataStore { DATA_STORE_FILE_NAME } }

    single<HttpClientEngine> { Js.create() }
}
