package com.ianarbuckle.gymplanner.android.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("MagicNumber")
fun LocalDate.displayFormattedDate(): String {
    val dayOfMonth = this.day
    val dayOfMonthSuffix =
        when {
            dayOfMonth in 11..13 -> "th"
            dayOfMonth % 10 == 1 -> "st"
            dayOfMonth % 10 == 2 -> "nd"
            dayOfMonth % 10 == 3 -> "rd"
            else -> "th"
        }

    val formatter =
        DateTimeFormatter.ofPattern("EEEE d'$dayOfMonthSuffix' MMMM, yyyy", Locale.getDefault())
    return this.toJavaLocalDate().format(formatter)
}

@Suppress("MagicNumber")
fun LocalDate.displayShortFormattedDate(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())
    return this.toJavaLocalDate().format(formatter)
}