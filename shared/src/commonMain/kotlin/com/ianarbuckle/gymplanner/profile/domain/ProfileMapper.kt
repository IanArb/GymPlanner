package com.ianarbuckle.gymplanner.profile.domain

import com.ianarbuckle.gymplanner.profile.dto.ProfileDto

object ProfileMapper {

    fun ProfileDto.toProfile(): Profile =
        Profile(
            userId = userId,
            username = username,
            firstName = firstName,
            surname = surname,
            email = email,
        )
}
