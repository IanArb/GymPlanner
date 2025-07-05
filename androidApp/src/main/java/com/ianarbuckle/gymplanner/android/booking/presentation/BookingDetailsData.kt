package com.ianarbuckle.gymplanner.android.booking.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.datetime.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
data class BookingDetailsData(
  val personalTrainerId: String,
  val timeSlotId: String,
  val selectedDate: String,
  val selectedTimeSlot: LocalTime,
  val personalTrainerName: String,
  val personalTrainerAvatarUrl: String,
  val location: String,
)
