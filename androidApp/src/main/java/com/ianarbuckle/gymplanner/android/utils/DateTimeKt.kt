package com.ianarbuckle.gymplanner.android.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun LocalDateTime.dayOfWeekDisplayName(
    textStyle: TextStyle,
    locale: Locale =  Locale.getDefault()
): String {
    return this.dayOfWeek.getDisplayName(textStyle, locale)
}

fun LocalDateTime.monthDisplayName(
    textStyle: TextStyle,
    locale: Locale =  Locale.getDefault()
): String {
    return this.month.getDisplayName(textStyle, locale)
}

fun differenceInDays(
    startDate: LocalDate,
    endDate: LocalDate
): Int {
    return startDate.daysUntil(endDate)
}

fun parseToLocalDateTime(
    input: String
): LocalDateTime {
    return LocalDateTime.parse(input, LocalDateTime.Formats.ISO)
}

fun String.toLocalTime(): LocalTime {
    return LocalTime.parse(this)
}

fun LocalTime.displayTime(): String {
    val formatter = DateTimeFormatter.ofPattern("h a", Locale.getDefault())
    return this.toJavaLocalTime().format(formatter).lowercase()
}