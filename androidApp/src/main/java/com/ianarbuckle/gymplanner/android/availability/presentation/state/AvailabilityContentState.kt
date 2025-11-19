package com.ianarbuckle.gymplanner.android.availability.presentation.state

import androidx.compose.runtime.Stable
import com.ianarbuckle.gymplanner.availability.domain.Time
import kotlinx.collections.immutable.ImmutableList

@Stable
data class AvailabilityContentState(
    val personalTrainerLabel: String,
    val name: String,
    val imageUrl: String,
    val qualifications: ImmutableList<String>,
    val daysOfWeek: ImmutableList<String>,
    val availableTimes: ImmutableList<Time>,
    val selectedDate: String,
    val selectedTimeSlot: String,
    val isAvailable: Boolean,
    val timeslotRowsPerPage: Int,
    val timeslotItemsPerPage: Int,
)
