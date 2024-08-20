package com.example.gymplanner.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.LocalDate
import org.mongodb.kbson.ObjectId

class GymPlanRealmDto : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var startDate: String = ""
    var endDate: String = ""
    var workouts: List<WorkoutRealmDto> = emptyList()
}

class WorkoutRealmDto : RealmObject {
    var name: String = ""
    var sets: Int = 0
    var repetitions: Int = 0
    var weight: Float = 0f
    var note: String = ""
}

