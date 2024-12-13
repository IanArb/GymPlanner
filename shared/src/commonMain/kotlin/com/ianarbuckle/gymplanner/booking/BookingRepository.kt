package com.ianarbuckle.gymplanner.booking

import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingMapper.toBookingDto
import com.ianarbuckle.gymplanner.booking.domain.BookingMapper.toBookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.serialization.SerializationException
import okio.IOException

class BookingRepository(
    private val remoteDataSource: BookingRemoteDataSource
) {

    suspend fun saveBooking(booking: Booking): Result<BookingResponse> {
        try {
            val result = remoteDataSource.saveBooking(booking.toBookingDto())
            val bookingResponse = result.toBookingResponse()
            return Result.success(bookingResponse)
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
        catch (ex: IOException) {
            return Result.failure(ex)
        }
        catch (ex: SerializationException) {
            return Result.failure(ex)
        }
    }

    suspend fun findBookingsByUserId(userId: String): Result<ImmutableList<BookingResponse>> {
        try {
            val booking = remoteDataSource.findBookingsByUserId(userId)
            val bookingResponse = booking.map {
                it.toBookingResponse()
            }.toImmutableList()

            return Result.success(bookingResponse)
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
        catch (ex: IOException) {
            return Result.failure(ex)
        }
        catch (ex: SerializationException) {
            return Result.failure(ex)
        }
        catch (ex: NoTransformationFoundException) {
            return Result.failure(ex)
        }
    }

}