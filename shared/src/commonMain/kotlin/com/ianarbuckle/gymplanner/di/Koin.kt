package com.ianarbuckle.gymplanner.di

import com.ianarbuckle.gymplanner.DefaultGymPlanner
import com.ianarbuckle.gymplanner.GymPlanner
import com.ianarbuckle.gymplanner.data.GymPlannerLocalDataSource
import com.ianarbuckle.gymplanner.data.GymPlannerRemoteDataSource
import com.ianarbuckle.gymplanner.data.GymPlannerRepository
import com.ianarbuckle.gymplanner.mapper.ClientMapper
import com.ianarbuckle.gymplanner.realm.ClientRealmDto
import com.ianarbuckle.gymplanner.realm.ExercisesRealmDto
import com.ianarbuckle.gymplanner.realm.GymPlanRealmDto
import com.ianarbuckle.gymplanner.realm.PersonalTrainerRealmDto
import com.ianarbuckle.gymplanner.realm.SessionRealmDto
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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    enableNetworkLogs: Boolean = false,
    appDeclaration: KoinAppDeclaration = {},
    baseUrl: String,
) {
    startKoin {
        appDeclaration()
        modules(
            networkModule(enableNetworkLogs = enableNetworkLogs, baseUrl = baseUrl),
            databaseModule(),
            gymPlannerModule()
        )
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

fun createJson() = Json { ignoreUnknownKeys = true }

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
    single { GymPlannerRemoteDataSource(httpClient = get()) }
    single { GymPlannerLocalDataSource(realm = get(), mapper = get()) }
    single { GymPlannerRepository(localDataSource = get()) }
    single { ClientMapper() }
    single<GymPlanner> { DefaultGymPlanner(repository = get() ) }
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
                PersonalTrainerRealmDto::class
            )
        )
        Realm.open(configuration)
    }
    single { realm }
}