package com.ianarbuckle.gymplanner.gymlocations.dto

import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import kotlinx.serialization.Serializable

@Serializable
data class GymLocationsDto(
    val id: String,
    val title: String,
    val subTitle: String,
    val description: String,
    val imageUrl: String,
) {

    fun transformToGymLocations(): GymLocations =
        GymLocations(
            title = title,
            subTitle = subTitle,
            description = description,
            imageUrl = imageUrl,
        )
}
