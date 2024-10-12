package com.ianarbuckle.gymplanner

expect object GymPlannerFactory {

    public fun create(baseUrl: String): GymPlanner

}