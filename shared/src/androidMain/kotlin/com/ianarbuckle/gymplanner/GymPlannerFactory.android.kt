package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.di.initKoin
import org.koin.java.KoinJavaComponent.get

actual object GymPlannerFactory {

    actual fun create(baseUrl: String): GymPlanner {
        initKoin(baseUrl = baseUrl)
        return get(GymPlanner::class.java)
    }
}