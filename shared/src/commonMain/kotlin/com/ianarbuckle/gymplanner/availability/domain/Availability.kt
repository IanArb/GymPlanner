package com.ianarbuckle.gymplanner.availability.domain

import kotlinx.collections.immutable.ImmutableList

data class Availability(
    val id: String,
    val month: String,
    val personalTrainerId: String,
    val slots: ImmutableList<Slot>
)

data class Slot(
    val date: String,
    val id: String,
    val times: ImmutableList<Time>
)

data class Time(
    val id: String,
    val endTime: String,
    val startTime: String,
    val status: String
)