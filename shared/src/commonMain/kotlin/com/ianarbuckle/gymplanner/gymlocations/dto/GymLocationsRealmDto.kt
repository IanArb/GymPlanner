package com.ianarbuckle.gymplanner.gymlocations.dto

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class GymLocationsRealmDto : RealmObject {
    @PrimaryKey
    var id: ObjectId = BsonObjectId()
    var title: String = ""
    var subTitle: String = ""
    var description: String = ""
    var imageUrl: String = ""
}
