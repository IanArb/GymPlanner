package com.ianarbuckle.gymplanner.di

import com.ianarbuckle.gymplanner.authentication.AuthenticationRemoteDataSource
import com.ianarbuckle.gymplanner.authentication.DefaultAuthenticationRemoteDataSource
import com.ianarbuckle.gymplanner.availability.AvailabilityRemoteDataSource
import com.ianarbuckle.gymplanner.availability.DefaultAvailabilityRemoteDataSource
import com.ianarbuckle.gymplanner.booking.BookingRemoteDataSource
import com.ianarbuckle.gymplanner.booking.DefaultBookingRemoteDataSource
import com.ianarbuckle.gymplanner.chat.ChatSocketService
import com.ianarbuckle.gymplanner.chat.ChatSocketServiceImpl
import com.ianarbuckle.gymplanner.chat.DefaultMessagesRemoteDataSource
import com.ianarbuckle.gymplanner.chat.MessagesRemoteDataSource
import com.ianarbuckle.gymplanner.faultreporting.DefaultFaultReportingRemoteDataSource
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRemoteDataSource
import com.ianarbuckle.gymplanner.fcm.DefaultFcmTokenRemoteDataSource
import com.ianarbuckle.gymplanner.fcm.FcmTokenRemoteDataSource
import com.ianarbuckle.gymplanner.fitnessclass.DefaultFitnessClassRemoteDataSource
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRemoteDataSource
import com.ianarbuckle.gymplanner.gymlocations.DefaultGymLocationsRemoteDataSource
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRemoteDataSource
import com.ianarbuckle.gymplanner.personaltrainers.DefaultPersonalTrainersRemoteDataSource
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRemoteDataSource
import com.ianarbuckle.gymplanner.profile.DefaultProfileRemoteDataSource
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
    baseUrl: String,
    websocketBaseUrl: String,
    appDeclaration: KoinAppDeclaration = {},
) {
    startKoin {
        appDeclaration()
        modules(
            networkModule(enableNetworkLogs = enableNetworkLogs, baseUrl = baseUrl),
            platformModule,
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
            fcmModule(baseUrl),
        )
    }
}

// init iOS
fun initKoinIOS(baseUrl: String, websocketBaseUrl: String) {
    initKoin(enableNetworkLogs = false, baseUrl = baseUrl, websocketBaseUrl = websocketBaseUrl) {}
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

fun fitnessClassModule(baseUrl: String) = module {
    single<FitnessClassRemoteDataSource> {
        DefaultFitnessClassRemoteDataSource(
            httpClient = get(),
            baseurl = baseUrl,
            dataStoreRepository = get(),
        )
    }
}

fun faultReportingModule(baseUrl: String) = module {
    single<FaultReportingRemoteDataSource> {
        DefaultFaultReportingRemoteDataSource(
            httpClient = get(),
            baseurl = baseUrl,
            dataStoreRepository = get(),
        )
    }
}

fun personalTrainersModule(baseUrl: String) = module {
    single<PersonalTrainersRemoteDataSource> {
        DefaultPersonalTrainersRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun gymLocationsModule(baseUrl: String) = module {
    single<GymLocationsRemoteDataSource> {
        DefaultGymLocationsRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun profileModule(baseUrl: String) = module {
    single<ProfileRemoteDataSource> {
        DefaultProfileRemoteDataSource(
            httpClient = get(),
            baseUrl = baseUrl,
            dataStoreRepository = get(),
        )
    }
}

fun authenticationModule(baseUrl: String) = module {
    single<AuthenticationRemoteDataSource> {
        DefaultAuthenticationRemoteDataSource(baseurl = baseUrl, httpClient = get())
    }
}

fun bookingModule(baseUrl: String) = module {
    single<BookingRemoteDataSource> {
        DefaultBookingRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun dataStoreModule() = module { single<DataStoreRepository> { DefaultDataStoreRepository() } }

fun availabilityModule(baseUrl: String) = module {
    single<AvailabilityRemoteDataSource> {
        DefaultAvailabilityRemoteDataSource(
            baseUrl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}

fun chatModule(baseUrl: String, websocketBaseUrl: String) = module {
    single<ChatSocketService> {
        ChatSocketServiceImpl(httpClient = get(), baseUrl = websocketBaseUrl)
    }
    single<MessagesRemoteDataSource> {
        DefaultMessagesRemoteDataSource(
            httpClient = get(),
            baseUrl = baseUrl,
            dataStoreRepository = get(),
        )
    }
}

fun fcmModule(baseUrl: String) = module {
    single<FcmTokenRemoteDataSource> {
        DefaultFcmTokenRemoteDataSource(
            baseurl = baseUrl,
            httpClient = get(),
            dataStoreRepository = get(),
        )
    }
}
