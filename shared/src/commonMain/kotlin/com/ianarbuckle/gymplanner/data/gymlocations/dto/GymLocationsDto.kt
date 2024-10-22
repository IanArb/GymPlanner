package com.ianarbuckle.gymplanner.data.gymlocations.dto

import kotlinx.serialization.Serializable

@Serializable
data class GymLocationsDto(
    val id: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val imageUrl: String,
)
