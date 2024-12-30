package com.ianarbuckle.gymplanner.fitnessclass.dto

import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class FitnessClassRealmDto : RealmObject {
    @PrimaryKey
    var id: ObjectId = BsonObjectId()
    var dayOfWeek: String = ""
    var description: String = ""
    var duration: DurationRealmDto? = null
    var endTime: String = ""
    var imageUrl: String = ""
    var name: String = ""
    var startTime: String = ""
}

class DurationRealmDto : EmbeddedRealmObject {
    var unit: String = ""
    var value: Int = 0
}
