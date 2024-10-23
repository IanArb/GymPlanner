package com.ianarbuckle.gymplanner.api

expect object GymPlannerFactory {

    public fun create(baseUrl: String): GymPlanner

}