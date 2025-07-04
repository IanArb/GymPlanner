package com.ianarbuckle.gymplanner.android.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun String.parseToLocalDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val localDate = java.time.LocalDate.parse(this, formatter)
    return localDate.toKotlinLocalDate()
}

fun String.toLocalTime(): LocalTime {
    return LocalTime.parse(this)
}

fun LocalTime.displayTime(): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    return this.toJavaLocalTime().format(formatter).lowercase()
}

fun LocalDateTime.calendarMonth(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.getDefault())
    return this.toJavaLocalDateTime().format(formatter)
}

@OptIn(ExperimentalTime::class)
@Suppress("MagicNumber")
fun currentWeekDates(): List<String> {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfWeek = currentDate.minus(currentDate.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

    return (0 until 7).map { dayOffset ->
        firstDayOfWeek.plus(dayOffset.toLong(), DateTimeUnit.DAY).toJavaLocalDate().format(dateFormat)
    }
}

@OptIn(ExperimentalTime::class)
fun String.isCurrentDay(): Boolean {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val currentDay = currentDate.toJavaLocalDate().format(dateFormat)
    return currentDay == this
}

fun convertDate(input: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    val outputFormatter = DateTimeFormatter.ofPattern("EEE\ndd", Locale.getDefault())
    val date = java.time.LocalDate.parse(input, inputFormatter)
    return date.format(outputFormatter)
}

@Suppress("MagicNumber")
fun LocalDate.displayFormattedDate(): String {
    val dayOfMonth = this.dayOfMonth
    val dayOfMonthSuffix = when {
        dayOfMonth in 11..13 -> "th"
        dayOfMonth % 10 == 1 -> "st"
        dayOfMonth % 10 == 2 -> "nd"
        dayOfMonth % 10 == 3 -> "rd"
        else -> "th"
    }

    val formatter = DateTimeFormatter.ofPattern("EEEE d'$dayOfMonthSuffix' MMMM, yyyy", Locale.getDefault())
    return this.toJavaLocalDate().format(formatter)
}

@Suppress("MagicNumber")
fun LocalDate.displayShortFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())
    return this.toJavaLocalDate().format(formatter)
}
