package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


fun String.parseToLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = java.time.LocalDate.parse(this, formatter)
    return localDate.toKotlinLocalDate()
}

fun String.toLocalTime(): LocalTime {
    return LocalTime.parse(this)
}

@OptIn(ExperimentalTime::class)
fun String.toDisplayTime(): String {
    val instantStr = if (this.endsWith("Z")) this else this + "Z"
    val instant = Instant.parse(instantStr)
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.time.displayTime()
}

fun convertDate(input: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("EEE\ndd", Locale.getDefault())
    val date = java.time.LocalDate.parse(input, inputFormatter)
    return date.format(outputFormatter)
}

fun String.toGymLocation(): GymLocation {
    return GymLocation.valueOf(this)
}

@OptIn(ExperimentalTime::class)
fun String.isCurrentDay(): Boolean {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val currentDay = currentDate.toJavaLocalDate().format(dateFormat)
    return currentDay == this
}