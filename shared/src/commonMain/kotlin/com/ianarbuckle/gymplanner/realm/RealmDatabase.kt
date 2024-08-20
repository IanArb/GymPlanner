package com.ianarbuckle.gymplanner.realm

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class RealmDatabase {

    private val realm: Realm by lazy {
        val configuration = RealmConfiguration.create(schema = setOf(GymPlanRealmDto::class, WorkoutRealmDto::class, ExercisesRealmDto::class, ))
        Realm.open(configuration)
    }

    fun createDatabase(): Realm = realm
}