package com.ianarbuckle.gymplanner.android.utils

import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime

fun LocalTime.displayTime(): String {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
    return this.toJavaLocalTime().format(formatter).lowercase()
}

fun LocalDateTime.calendarMonth(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM", Locale.getDefault())
    return this.toJavaLocalDateTime().format(formatter)
}
