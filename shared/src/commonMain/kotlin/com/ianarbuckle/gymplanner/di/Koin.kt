package com.ianarbuckle.gymplanner.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.authentication.AuthenticationRemoteDataSource
import com.ianarbuckle.gymplanner.availability.AvailabilityRemoteDataSource
import com.ianarbuckle.gymplanner.booking.BookingRemoteDataSource
import com.ianarbuckle.gymplanner.chat.ChatSocketService
import com.ianarbuckle.gymplanner.chat.ChatSocketServiceImpl
import com.ianarbuckle.gymplanner.chat.MessagesRemoteDataSource
import com.ianarbuckle.gymplanner.clients.ClientsRemoteDataSource
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRemoteDataSource
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRemoteDataSource
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRemoteDataSource
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRemoteDataSource
import com.ianarbuckle.gymplanner.profile.ProfileRemoteDataSource
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.DefaultDataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    enableNetworkLogs: Boolean = false,
    appDeclaration: KoinAppDeclaration = {},
    baseUrl: String,
    websocketBaseUrl: String,
    dataStore: DataStore<Preferences>,
) {
    startKoin {
        appDeclaration()
        modules(
            networkModule(enableNetworkLogs = enableNetworkLogs, baseUrl = baseUrl),
            databaseModule(dataStore),
            clientsModule(baseUrl),
            fitnessClassModule(baseUrl),
            faultReportingModule(baseUrl),
            personalTrainersModule(baseUrl),
            gymLocationsModule(baseUrl),
            authenticationModule(baseUrl),
            dataStoreModule(),
            profileModule(baseUrl),
            bookingModule(baseUrl),
            availabilityModule(baseUrl),
            chatModule(baseUrl = baseUrl, websocketBaseUrl = websocketBaseUrl),
        )
    }
}

fun networkModule(enableNetworkLogs: Boolean, baseUrl: String) = module {
    singleOf(::createJson)
    single {
        createHttpClient(json = get(), enableNetworkLogs = enableNetworkLogs, baseUrl = baseUrl)
    }
}

fun createJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun createHttpClient(json: Json, enableNetworkLogs: Boolean, baseUrl: String) =
    HttpClient(CIO) {
        install(ContentNegotiation) { json(json) }
        install(WebSockets)
        if (enableNetworkLogs) {
            install(Logging) {}
        }

        defaultRequest { url(baseUrl) }
    }

fun clientsModule(baseUrl: String) = module {
    single {
        ClientsRemoteDataSource(httpClient = get(), baseurl = baseUrl, dataStoreRepository = get())
    }
}

fun fitnessClassModule(baseUrl: String) = module {
    single {
        FitnessClassRemoteDataSource(
            httpClient = get(),
            baseurl = baseUrl,
            dataStoreRepository = get(),
        )
    }
}

fun faultReportingModule(baseUrl: String) = module {
    single {
        FaultReportingRemoteDataSource(
            httpClient = get(),
            baseurl = baseUrl,
            dataStoreRepository = get(),
        )
    }
}

fun personalTrainersModule(baseUrl: String) = module {
    single {
        PersonalTrainersRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun gymLocationsModule(baseUrl: String) = module {
    single {
        GymLocationsRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun profileModule(baseUrl: String) = module {
    single {
        ProfileRemoteDataSource(httpClient = get(), baseUrl = baseUrl, dataStoreRepository = get())
    }
}

fun authenticationModule(baseUrl: String) = module {
    single { AuthenticationRemoteDataSource(baseurl = baseUrl, httpClient = get()) }
}

fun bookingModule(baseUrl: String) = module {
    single {
        BookingRemoteDataSource(baseUrl = baseUrl, httpClient = get(), dataStoreRepository = get())
    }
}

fun dataStoreModule() = module { single<DataStoreRepository> { DefaultDataStoreRepository() } }

fun availabilityModule(baseUrl: String) = module {
    single {
        AvailabilityRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun databaseModule(dataStore: DataStore<Preferences>) = module { single { dataStore } }

fun chatModule(baseUrl: String, websocketBaseUrl: String) = module {
    single<ChatSocketService> {
        ChatSocketServiceImpl(httpClient = get(), baseUrl = websocketBaseUrl)
    }
    single {
        MessagesRemoteDataSource(httpClient = get(), baseUrl = baseUrl, dataStoreRepository = get())
    }
}
