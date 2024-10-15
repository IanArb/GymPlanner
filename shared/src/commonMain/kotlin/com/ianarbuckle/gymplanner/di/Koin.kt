package com.ianarbuckle.gymplanner.di

import com.ianarbuckle.gymplanner.DefaultGymPlanner
import com.ianarbuckle.gymplanner.GymPlanner
import com.ianarbuckle.gymplanner.data.clients.clients.ClientsLocalDataSource
import com.ianarbuckle.gymplanner.data.clients.clients.ClientsRemoteDataSource
import com.ianarbuckle.gymplanner.data.clients.clients.ClientsRepository
import com.ianarbuckle.gymplanner.data.faultreporting.FaultReportingRemoteDataSource
import com.ianarbuckle.gymplanner.data.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.data.fitnessclass.FitnessClassLocalDataSource
import com.ianarbuckle.gymplanner.data.fitnessclass.FitnessClassRemoteDataSource
import com.ianarbuckle.gymplanner.data.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.realm.ClientRealmDto
import com.ianarbuckle.gymplanner.realm.DurationRealmDto
import com.ianarbuckle.gymplanner.realm.ExercisesRealmDto
import com.ianarbuckle.gymplanner.realm.FitnessClassRealmDto
import com.ianarbuckle.gymplanner.realm.GymPlanRealmDto
import com.ianarbuckle.gymplanner.realm.PersonalTrainerRealmDto
import com.ianarbuckle.gymplanner.realm.SessionRealmDto
import com.ianarbuckle.gymplanner.realm.WeightRealmDto
import com.ianarbuckle.gymplanner.realm.WorkoutRealmDto
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
                faultReportingModule(baseUrl)
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
        )
    }
}

fun clientsModule(baseUrl: String) = module {
    single { ClientsRemoteDataSource(httpClient = get(), baseurl = baseUrl) }
    single { ClientsLocalDataSource(realm = get()) }
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

fun databaseModule() = module {
    val realm: Realm by lazy {
        val configuration = RealmConfiguration.create(
            schema = setOf(
                GymPlanRealmDto::class,
                WorkoutRealmDto::class,
                ExercisesRealmDto::class,
                SessionRealmDto::class,
                ClientRealmDto::class,
                PersonalTrainerRealmDto::class,
                WeightRealmDto::class,
                FitnessClassRealmDto::class,
                DurationRealmDto::class
            )
        )
        Realm.open(configuration)
    }
    single { realm }
}