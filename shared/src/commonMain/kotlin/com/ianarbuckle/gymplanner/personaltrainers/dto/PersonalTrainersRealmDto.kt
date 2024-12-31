package com.ianarbuckle.gymplanner.personaltrainers.dto

import com.ianarbuckle.gymplanner.clients.dto.GymLocationRealmDto
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmMap
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore

class PersonalTrainersRealmDto : RealmObject {
    var id: String = ""
    var firstName: String = ""
    var lastName: String = ""
    var bio: String = ""
    var imageUrl: String = ""

    @Ignore
    var socials: RealmMap<String, String> = realmDictionaryOf()
    var qualifications: RealmList<String> = realmListOf()
    var gymLocation: GymLocationRealmDto? = null
}
