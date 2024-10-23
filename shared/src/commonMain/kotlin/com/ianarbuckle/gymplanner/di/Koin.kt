package com.ianarbuckle.gymplanner.di

import com.ianarbuckle.gymplanner.api.DefaultGymPlanner
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.clients.ClientsRemoteDataSource
import com.ianarbuckle.gymplanner.clients.ClientsRepository
import com.ianarbuckle.gymplanner.clients.dto.ClientRealmDto
import com.ianarbuckle.gymplanner.clients.dto.GymLocationRealmDto
import com.ianarbuckle.gymplanner.clients.dto.GymPlanRealmDto
import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerRealmDto
import com.ianarbuckle.gymplanner.clients.dto.SessionRealmDto
import com.ianarbuckle.gymplanner.clients.dto.WeightRealmDto
import com.ianarbuckle.gymplanner.clients.dto.WorkoutRealmDto
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRemoteDataSource
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassLocalDataSource
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRemoteDataSource
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.dto.DurationRealmDto
import com.ianarbuckle.gymplanner.fitnessclass.dto.FitnessClassRealmDto
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsLocalDataSource
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRemoteDataSource
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsRealmDto
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersLocalDataSource
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRemoteDataSource
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    enableNetworkLogs: Boolean = false,
    appDeclaration: KoinAppDeclaration = {},
    baseUrl: String,
) {
    try {
        startKoin {
            appDeclaration()
            modules(
                networkModule(enableNetworkLogs = enableNetworkLogs, baseUrl = baseUrl),
                databaseModule(),
                gymPlannerModule(),
                clientsModule(baseUrl),
                fitnessClassModule(baseUrl),
                faultReportingModule(baseUrl),
                personalTrainersModule(baseUrl),
                gymLocationsModule(baseUrl)
            )
        }
    } catch (ex: KoinAppAlreadyStartedException) {
        println("Koin app is already started.")
    }

}

fun networkModule(enableNetworkLogs: Boolean, baseUrl: String) = module {
    singleOf(::createJson)
    single {
        createHttpClient(
            json = get(),
            enableNetworkLogs = enableNetworkLogs,
            baseUrl = baseUrl
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

fun gymPlannerModule() = module {
    single<GymPlanner> {
        DefaultGymPlanner(
            clientsRepository = get(),
            fitnessClassRepository = get(),
            faultReportingRepository = get(),
            personalTrainersRepository = get(),
            gymLocationsRepository = get()
        )
    }
}

fun clientsModule(baseUrl: String) = module {
    single { ClientsRemoteDataSource(httpClient = get(), baseurl = baseUrl) }
    single { com.ianarbuckle.gymplanner.clients.ClientsLocalDataSource(realm = get()) }
    single { ClientsRepository(localDataSource = get(), remoteDataSource = get()) }
}

fun fitnessClassModule(baseUrl: String) = module {
    single { FitnessClassLocalDataSource(realm = get()) }
    single { FitnessClassRemoteDataSource(httpClient = get(), baseurl = baseUrl) }
    single { FitnessClassRepository(localDataSource = get(), remoteDataSource = get()) }
}

fun faultReportingModule(baseUrl: String) = module {
    single { FaultReportingRemoteDataSource(httpClient = get(), baseurl = baseUrl) }
    single { FaultReportingRepository(remoteDataSource = get()) }
}

fun personalTrainersModule(baseUrl: String) = module {
    single { PersonalTrainersRemoteDataSource(baseUrl = baseUrl, httpClient = get()) }
    single { PersonalTrainersLocalDataSource(realm = get()) }
    single { PersonalTrainersRepository(remoteDataSource = get(), localDataSource = get()) }
}

fun gymLocationsModule(baseUrl: String) = module {
    single { GymLocationsRemoteDataSource(baseUrl = baseUrl, httpClient = get()) }
    single { GymLocationsLocalDataSource(realm = get()) }
    single { GymLocationsRepository(remoteDataSource = get(), localDataSource = get()) }
}

fun databaseModule() = module {
    val realm: Realm by lazy {
        val configuration = RealmConfiguration.create(
            schema = setOf(
                GymPlanRealmDto::class,
                WorkoutRealmDto::class,
                SessionRealmDto::class,
                ClientRealmDto::class,
                PersonalTrainerRealmDto::class,
                WeightRealmDto::class,
                FitnessClassRealmDto::class,
                DurationRealmDto::class,
                GymLocationRealmDto::class,
                GymLocationsRealmDto::class
            )
        )
        Realm.open(configuration)
    }
    single { realm }
}