package com.ianarbuckle.gymplanner.api

import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

interface GymPlanner {

    suspend fun fetchAllClients(): Result<List<Client>>

    suspend fun saveClient(client: Client): Result<Client>

    suspend fun findClientById(id: String): Result<Client>

    suspend fun deleteClient(id: String): Result<Unit>

    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>>

    suspend fun fetchTodaysFitnessClasses(): Result<List<FitnessClass>>

    suspend fun submitFault(fault: FaultReport): Result<FaultReport>

    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<List<PersonalTrainer>>

    suspend fun fetchGymLocations(): Result<List<GymLocations>>

    suspend fun login(login: Login): Result<LoginResponse>

    suspend fun saveAuthToken(token: String)

    suspend fun saveRememberMe(rememberMe: Boolean)

    suspend fun fetchAuthToken(): String

    suspend fun fetchRememberMe(): Boolean
}