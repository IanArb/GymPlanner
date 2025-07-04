package com.ianarbuckle.gymplanner.booking

import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingMapper.toBookingDto
import com.ianarbuckle.gymplanner.booking.domain.BookingMapper.toBookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CancellationException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BookingRepository {
  suspend fun saveBooking(booking: Booking): Result<BookingResponse>

  suspend fun findBookingsByUserId(userId: String): Result<ImmutableList<BookingResponse>>
}

class DefaultBookingRepository : BookingRepository, KoinComponent {

  private val remoteDataSource: BookingRemoteDataSource by inject()

  override suspend fun saveBooking(booking: Booking): Result<BookingResponse> {
    try {
      val result = remoteDataSource.saveBooking(booking.toBookingDto())
      val bookingResponse = result.toBookingResponse()
      return Result.success(bookingResponse)
    } catch (ex: Exception) {
      if (ex is CancellationException) {
        throw ex
      }
      Logger.withTag("BookingRepository").e("Error saving booking: $ex")
      return Result.failure(ex)
    }
  }

  override suspend fun findBookingsByUserId(
    userId: String
  ): Result<ImmutableList<BookingResponse>> {
    try {
      val booking = remoteDataSource.findBookingsByUserId(userId)
      val bookingResponse = booking.map { it.toBookingResponse() }.toImmutableList()

      return Result.success(bookingResponse)
    } catch (ex: Exception) {
      if (ex is CancellationException) {
        throw ex
      }
      Logger.e("Error fetching bookings: $ex")
      return Result.failure(ex)
    }
  }
}
