package com.ianarbuckle.gymplanner.model.realm

import io.realm.kotlin.types.RealmObject

class ExercisesRealmDto : RealmObject {
    var id: String = ""
    var name: String = ""
    var category: String = ""
    var description: String = ""
}