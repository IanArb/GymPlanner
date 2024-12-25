package com.ianarbuckle.gymplanner.gymlocations

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocationsMapper.transformToGymLocations
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CancellationException
import okio.IOException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface GymLocationsRepository {
    suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>>
}

class DefaultGymLocationsRepository : GymLocationsRepository, KoinComponent {

    private val localDataSource: GymLocationsLocalDataSource by inject()
    private val remoteDataSource: GymLocationsRemoteDataSource by inject()

    override suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>> {
        try {
            val remoteGymLocations = remoteDataSource.gymLocations()
            remoteGymLocations.map { gymLocation ->
                localDataSource.saveGymLocation(gymLocation.transformToGymLocations())
            }
            val gymLocations = remoteGymLocations.map { it.transformToGymLocations() }.toImmutableList()
            return Result.success(gymLocations)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            Logger.withTag("GymLocationsRepository").e("Error fetching gym locations: $ex")
            return Result.failure(ex)
        }
    }
}