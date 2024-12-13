package com.ianarbuckle.gymplanner.android.utils

import android.icu.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Calendar.currentMonth(): String {
    val month = get(Calendar.MONTH)
    return when (month) {
        0 -> "January"
        1 -> "February"
        2 -> "March"
        3 -> "April"
        4 -> "May"
        5 -> "June"
        6 -> "July"
        7 -> "August"
        8 -> "September"
        9 -> "October"
        10 -> "November"
        11 -> "December"
        else -> "Unknown"
    }
}

fun Calendar.currentWeekDates(): List<String> {
    val dateFormat = SimpleDateFormat("EEE\ndd", Locale.getDefault())

    // Set the calendar to the start of the week
    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)

    val weekDates = mutableListOf<String>()
    for (i in 0 until 7) {
        weekDates.add(dateFormat.format(time))
        add(Calendar.DAY_OF_WEEK, 1)
    }

    return weekDates
}

fun Calendar.isCurrentDay(day: String): Boolean {
    val dateFormat = SimpleDateFormat("EEE\ndd", Locale.getDefault())
    val currentDay = dateFormat.format(time)
    return currentDay == day
}

fun Calendar.currentDay(): String {
    val dateFormat = SimpleDateFormat("EEE\ndd", Locale.getDefault())
    return dateFormat.format(time)
}

fun Calendar.datesInFuture(dates: List<String>): List<String> {
    val dateFormat = SimpleDateFormat("EEE\ndd", Locale.getDefault())
    val currentDate = time

    return dates.filter { date ->
        val inputDate = dateFormat.parse(date)
        inputDate != null && inputDate.after(currentDate)
    }
}