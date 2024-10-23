package com.ianarbuckle.gymplanner.api

import com.ianarbuckle.gymplanner.di.initKoin
import org.koin.java.KoinJavaComponent

actual object GymPlannerFactory {

    actual fun create(baseUrl: String): GymPlanner {
        initKoin(baseUrl = baseUrl)
        return KoinJavaComponent.get(GymPlanner::class.java)
    }
}