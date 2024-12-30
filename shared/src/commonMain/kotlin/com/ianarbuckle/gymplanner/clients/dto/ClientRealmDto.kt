package com.ianarbuckle.gymplanner.clients.dto

import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmMap
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ClientRealmDto : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var firstName: String = ""
    var surname: String = ""
    var strengthLevel: String = ""
    var gymPlan: GymPlanRealmDto? = null
}

class GymPlanRealmDto : EmbeddedRealmObject {
    var name: String = ""
    var personalTrainer: PersonalTrainerRealmDto? = null
    var startDate: String = ""
    var endDate: String = ""
    var sessions: RealmList<SessionRealmDto> = realmListOf()
}

class PersonalTrainerRealmDto : EmbeddedRealmObject {
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

class GymLocationRealmDto : RealmObject {
    private var enumDescription: String? = null

    fun saveEnum(value: GymLocationEnumRealm) {
        this.enumDescription = value.toString()
    }

    val enum: GymLocationEnumRealm?
        get() = if ((enumDescription != null)) {
            GymLocationEnumRealm.valueOf(
                enumDescription!!,
            )
        } else {
            null
        }
}

enum class GymLocationEnumRealm {
    CLONTARF,
    ASTONQUAY,
    LEOPARDSTOWN,
    DUNLOAGHAIRE,
    UNKNOWN,
}

class SessionRealmDto : EmbeddedRealmObject {
    var name: String = ""
    var workouts: RealmList<WorkoutRealmDto> = realmListOf()
}

class WorkoutRealmDto : EmbeddedRealmObject {
    var name: String = ""
    var sets: Int = 0
    var repetitions: Int = 0
    var weight: WeightRealmDto? = null
    var note: String = ""
}

class WeightRealmDto : EmbeddedRealmObject {
    var value: Double = 0.0
    var unit: String = ""
}
