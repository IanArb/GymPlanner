package com.ianarbuckle.gymplanner.gymlocations.domain

import com.ianarbuckle.gymplanner.gymlocations.dto.GymLocationsDto

object GymLocationsMapper {

  fun GymLocationsDto.transformToGymLocations(): GymLocations =
    GymLocations(title = title, subTitle = subTitle, description = description, imageUrl = imageUrl)
}
