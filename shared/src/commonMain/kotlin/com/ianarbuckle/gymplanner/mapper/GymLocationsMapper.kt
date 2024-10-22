package com.ianarbuckle.gymplanner.mapper

import com.ianarbuckle.gymplanner.data.gymlocations.dto.GymLocationsDto
import com.ianarbuckle.gymplanner.model.GymLocations
import com.ianarbuckle.gymplanner.realm.GymLocationsRealmDto
import io.realm.kotlin.notifications.ResultsChange

object GymLocationsMapper {

    fun GymLocationsDto.transformToGymLocations(): GymLocations {
        return GymLocations(
            title = title,
            subTitle = subTitle,
            description = description,
            imageUrl = imageUrl
        )
    }

    fun GymLocations.transformToGymLocationsRealm(): GymLocationsRealmDto {
        return GymLocationsRealmDto().apply {
            val gymLocations = this@transformToGymLocationsRealm
            title = gymLocations.title
            subTitle = gymLocations.subTitle
            description = gymLocations.description
            imageUrl = gymLocations.imageUrl
        }
    }

    fun ResultsChange<GymLocationsRealmDto>.transformToGymLocations(): List<GymLocations> {
        return this.list.map { gymLocations ->
            GymLocations(
                title = gymLocations.title,
                subTitle = gymLocations.subTitle,
                description = gymLocations.description,
                imageUrl = gymLocations.imageUrl
            )
        }
    }

}