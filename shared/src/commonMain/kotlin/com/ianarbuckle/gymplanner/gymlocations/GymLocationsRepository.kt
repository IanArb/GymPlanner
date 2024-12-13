package com.ianarbuckle.gymplanner.gymlocations

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
import okio.IOException


class GymLocationsRepository(
    private val localDataSource: GymLocationsLocalDataSource,
    private val remoteDataSource: GymLocationsRemoteDataSource,
) {

    suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>> {
        try {
            val remoteGymLocations = remoteDataSource.gymLocations()
            remoteGymLocations.map { gymLocation ->
                localDataSource.saveGymLocation(gymLocation.transformToGymLocations())
            }
            val gymLocations = remoteGymLocations.map { it.transformToGymLocations() }.toImmutableList()
            return Result.success(gymLocations)
        } catch (ex: ClientRequestException) {
            return Result.failure(ex)
        }
        catch (ex: ServerResponseException) {
            return Result.failure(ex)
        }
        catch (ex: HttpRequestTimeoutException) {
            return Result.failure(ex)
        }
        catch (ex: ResponseException) {
            return Result.failure(ex)
        }
        catch (ex: NoTransformationFoundException) {
            return Result.failure(ex)
        }
        catch (ex: IOException) {
            return Result.failure(ex)
        }
    }
}