package com.ianarbuckle.gymplanner.model.realm

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
    var surname: String = ""
    @Ignore
    var socials: RealmMap<String, String> = realmDictionaryOf()
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

