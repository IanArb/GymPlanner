package com.example.gymplanner

expect object GymPlannerFactory {

    public fun create(baseUrl: String): GymPlanner
}