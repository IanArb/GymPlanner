package com.ianarbuckle.gymplanner.personaltrainers.domain

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.clients.dto.GymLocationDto
import com.ianarbuckle.gymplanner.clients.dto.GymLocationEnumRealm
import com.ianarbuckle.gymplanner.clients.dto.GymLocationRealmDto
import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerDto
import com.ianarbuckle.gymplanner.clients.dto.PersonalTrainerRealmDto
import com.ianarbuckle.gymplanner.personaltrainers.dto.PersonalTrainersRealmDto
import io.realm.kotlin.ext.toRealmDictionary
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.types.RealmMap

object PersonalTrainerMapper {

    fun PersonalTrainerDto.toPersonalTrainer(): PersonalTrainer {
        return PersonalTrainer(
            firstName = firstName,
            lastName = lastName,
            imageUrl = imageUrl,
            bio = bio,
            socials = socials,
            gymLocation = gymLocation.toGymLocation()
        )
    }

    private fun GymLocationDto.toGymLocation(): GymLocation {
        return this.let {
            when (it) {
                GymLocationDto.CLONTARF -> GymLocation.CLONTARF
                GymLocationDto.ASTONQUAY -> GymLocation.ASTONQUAY
                GymLocationDto.LEOPARDSTOWN -> GymLocation.LEOPARDSTOWN
                GymLocationDto.DUNLOAGHAIRE -> GymLocation.DUNLOAGHAIRE
                GymLocationDto.UNKNOWN -> GymLocation.UNKNOWN
            }
        }
    }

    fun mapPersonalTrainers(personalTrainer: PersonalTrainer?): PersonalTrainersRealmDto {
        return PersonalTrainersRealmDto().apply {
            firstName = personalTrainer?.firstName ?: ""
            lastName = personalTrainer?.lastName ?: ""
            bio = personalTrainer?.bio ?: ""
            imageUrl = personalTrainer?.imageUrl ?: ""
            gymLocation = mapGymLocation(gymLocation = personalTrainer?.gymLocation)
            socials = mapSocials(personalTrainer?.socials ?: emptyMap())
        }
    }

    fun mapPersonalTrainer(personalTrainer: PersonalTrainer?): PersonalTrainerRealmDto {
        return PersonalTrainerRealmDto().apply {
            firstName = personalTrainer?.firstName ?: ""
            lastName = personalTrainer?.lastName ?: ""
            bio = personalTrainer?.bio ?: ""
            imageUrl = personalTrainer?.imageUrl ?: ""
            gymLocation = mapGymLocation(gymLocation = personalTrainer?.gymLocation)
            socials = mapSocials(personalTrainer?.socials ?: emptyMap())
        }
    }

    private fun mapGymLocation(gymLocation: GymLocation?): GymLocationRealmDto {
        val enums = GymLocationEnumRealm.entries.toTypedArray().map { gymLocationEnum ->
            gymLocation to gymLocationEnum
        }

        return GymLocationRealmDto().apply {
            saveEnum(enums.associate { it.first to it.second }[gymLocation] ?: GymLocationEnumRealm.CLONTARF)
        }
    }

    private fun mapSocials(socials: Map<String, String>): RealmMap<String, String> {
        return socials.toRealmDictionary()
    }

    fun transformToPersonalTrainerModel(personalTrainer: PersonalTrainerRealmDto?): PersonalTrainerDto {
        return PersonalTrainerDto(
            id = personalTrainer?.id ?: "",
            firstName = personalTrainer?.firstName ?: "",
            lastName = personalTrainer?.lastName ?: "",
            bio = personalTrainer?.bio ?: "",
            imageUrl = personalTrainer?.imageUrl ?: "",
            socials = personalTrainer?.socials ?: emptyMap(),
            gymLocation = transformGymLocationDtoToEnum(gymLocationRealm = personalTrainer?.gymLocation) ?: GymLocationDto.UNKNOWN
        )
    }

    fun ResultsChange<PersonalTrainersRealmDto>.transformToPersonalTrainers(): List<PersonalTrainer> {
        return list.map {
            PersonalTrainer(
                id = it.id,
                firstName = it.firstName,
                lastName = it.lastName,
                bio = it.bio ?: "",
                imageUrl = it.imageUrl,
                socials = it.socials,
                gymLocation = transformGymLocationToEnum(gymLocationRealm = it.gymLocation) ?: GymLocation.UNKNOWN
            )
        }
    }

    private fun transformGymLocationToEnum(gymLocationRealm: GymLocationRealmDto?): GymLocation? {
        return gymLocationRealm?.enum?.let {
            when (it) {
                GymLocationEnumRealm.CLONTARF -> GymLocation.CLONTARF
                GymLocationEnumRealm.ASTONQUAY -> GymLocation.ASTONQUAY
                GymLocationEnumRealm.LEOPARDSTOWN -> GymLocation.LEOPARDSTOWN
                GymLocationEnumRealm.DUNLOAGHAIRE -> GymLocation.DUNLOAGHAIRE
                GymLocationEnumRealm.UNKNOWN -> GymLocation.UNKNOWN
            }
        }
    }

    private fun transformGymLocationDtoToEnum(gymLocationRealm: GymLocationRealmDto?): GymLocationDto? {
        return gymLocationRealm?.enum?.let {
            when (it) {
                GymLocationEnumRealm.CLONTARF -> GymLocationDto.CLONTARF
                GymLocationEnumRealm.ASTONQUAY -> GymLocationDto.ASTONQUAY
                GymLocationEnumRealm.LEOPARDSTOWN -> GymLocationDto.LEOPARDSTOWN
                GymLocationEnumRealm.DUNLOAGHAIRE -> GymLocationDto.DUNLOAGHAIRE
                GymLocationEnumRealm.UNKNOWN -> GymLocationDto.UNKNOWN
            }
        }
    }
}