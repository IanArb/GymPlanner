package com.ianarbuckle.gymplanner.android.utils

import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalTime::class)
@Suppress("MagicNumber")
fun currentWeekDates(): List<String> {
    val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val firstDayOfWeek = currentDate.minus(currentDate.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)
    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

    return (0 until 7).map { dayOffset ->
        firstDayOfWeek
            .plus(dayOffset.toLong(), DateTimeUnit.DAY)
            .toJavaLocalDate()
            .format(dateFormat)
    }
}
