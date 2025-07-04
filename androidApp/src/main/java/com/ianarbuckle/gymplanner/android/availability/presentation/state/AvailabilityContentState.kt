package com.ianarbuckle.gymplanner.android.availability.presentation.state

import com.ianarbuckle.gymplanner.availability.domain.Time
import kotlinx.collections.immutable.ImmutableList

data class AvailabilityContentState(
  val personalTrainerLabel: String,
  val name: String,
  val imageUrl: String,
  val qualifications: List<String>,
  val daysOfWeek: ImmutableList<String>,
  val availableTimes: ImmutableList<Time>,
  val selectedDate: String,
  val selectedTimeSlot: String,
  val isAvailable: Boolean,
  val timeslotRowsPerPage: Int,
  val timeslotItemsPerPage: Int,
)
