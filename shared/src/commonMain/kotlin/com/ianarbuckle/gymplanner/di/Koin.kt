package com.ianarbuckle.gymplanner.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.authentication.AuthenticationRemoteDataSource
import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.DefaultAuthenticationRepository
import com.ianarbuckle.gymplanner.availability.AvailabilityRemoteDataSource
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.DefaultAvailabilityRepository
import com.ianarbuckle.gymplanner.booking.BookingRemoteDataSource
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.DefaultBookingRepository
import com.ianarbuckle.gymplanner.clients.ClientsRemoteDataSource
import com.ianarbuckle.gymplanner.clients.ClientsRepository
import com.ianarbuckle.gymplanner.clients.DefaultClientsRepository
import com.ianarbuckle.gymplanner.faultreporting.DefaultFaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRemoteDataSource
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.fitnessclass.DefaultFitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRemoteDataSource
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.gymlocations.DefaultGymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRemoteDataSource
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import com.ianarbuckle.gymplanner.personaltrainers.DefaultPersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRemoteDataSource
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.profile.DefaultProfileRepository
import com.ianarbuckle.gymplanner.profile.ProfileRemoteDataSource
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.DefaultDataStoreRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
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
        )
    }
}

fun networkModule(enableNetworkLogs: Boolean, baseUrl: String) = module {
    singleOf(::createJson)
    single {
        createHttpClient(
            json = get(),
            enableNetworkLogs = enableNetworkLogs,
            baseUrl = baseUrl,
        )
    }
}

fun createJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
}

fun createHttpClient(
    json: Json,
    enableNetworkLogs: Boolean,
    baseUrl: String,
) = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    if (enableNetworkLogs) {
        install(Logging) {
        }
    }

    defaultRequest {
        url(baseUrl)
    }
}

fun clientsModule(baseUrl: String) = module {
    single { ClientsRemoteDataSource(httpClient = get(), baseurl = baseUrl, dataStoreRepository = get()) }
    single<ClientsRepository> { DefaultClientsRepository() }
}

fun fitnessClassModule(baseUrl: String) = module {
    single { FitnessClassRemoteDataSource(httpClient = get(), baseurl = baseUrl, dataStoreRepository = get()) }
    single<FitnessClassRepository> { DefaultFitnessClassRepository() }
}

fun faultReportingModule(baseUrl: String) = module {
    single { FaultReportingRemoteDataSource(httpClient = get(), baseurl = baseUrl, dataStoreRepository = get()) }
    single<FaultReportingRepository> { DefaultFaultReportingRepository() }
}

fun personalTrainersModule(baseUrl: String) = module {
    single { PersonalTrainersRemoteDataSource(baseUrl = baseUrl, httpClient = get(), dataStoreRepository = get()) }
    single<PersonalTrainersRepository> { DefaultPersonalTrainersRepository() }
}

fun gymLocationsModule(baseUrl: String) = module {
    single { GymLocationsRemoteDataSource(baseUrl = baseUrl, httpClient = get(), dataStoreRepository = get()) }
    single<GymLocationsRepository> { DefaultGymLocationsRepository() }
}

fun profileModule(baseUrl: String) = module {
    single {
        ProfileRemoteDataSource(
            httpClient = get(),
            baseUrl = baseUrl,
            dataStoreRepository = get(),
        )
    }
    single<ProfileRepository> {
        DefaultProfileRepository()
    }
}

fun authenticationModule(baseUrl: String) = module {
    single {
        AuthenticationRemoteDataSource(
            baseurl = baseUrl,
            httpClient = get(),
        )
    }
    single<AuthenticationRepository> {
        DefaultAuthenticationRepository()
    }
}

fun bookingModule(baseUrl: String) = module {
    single { BookingRemoteDataSource(baseUrl = baseUrl, httpClient = get(), dataStoreRepository = get()) }
    single<BookingRepository> { DefaultBookingRepository() }
}

fun dataStoreModule() = module {
    single<DataStoreRepository> { DefaultDataStoreRepository() }
}

fun availabilityModule(baseUrl: String) = module {
    single { AvailabilityRemoteDataSource(baseUrl = baseUrl, httpClient = get(), dataStoreRepository = get()) }
    single<AvailabilityRepository> { DefaultAvailabilityRepository() }
}

fun databaseModule(dataStore: DataStore<Preferences>) = module {
    single { dataStore }
}
