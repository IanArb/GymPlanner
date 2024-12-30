package com.ianarbuckle.gymplanner.api

import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.profile.domain.Profile
import kotlinx.collections.immutable.ImmutableList

interface GymPlanner {

    suspend fun fetchAllClients(): Result<ImmutableList<Client>>

    suspend fun saveClient(client: Client): Result<Client>

    suspend fun findClientById(id: String): Result<Client>

    suspend fun deleteClient(id: String): Result<Unit>

    suspend fun fetchFitnessClasses(dayOfWeek: String): Result<ImmutableList<FitnessClass>>

    suspend fun fetchTodaysFitnessClasses(): Result<ImmutableList<FitnessClass>>

    suspend fun submitFault(fault: FaultReport): Result<FaultReport>

    suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<ImmutableList<PersonalTrainer>>

    suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>>

    suspend fun login(login: Login): Result<LoginResponse>

    suspend fun saveAuthToken(token: String)

    suspend fun saveUserId(userId: String)

    suspend fun fetchUserId(): String

    suspend fun saveRememberMe(rememberMe: Boolean)

    suspend fun fetchAuthToken(): String

    suspend fun fetchRememberMe(): Boolean

    suspend fun fetchProfile(userId: String): Result<Profile>

    suspend fun saveBooking(booking: Booking): Result<BookingResponse>

    suspend fun fetchBookingsByUserId(userId: String): Result<ImmutableList<BookingResponse>>

    suspend fun fetchAvailability(personalTrainerId: String, month: String): Result<Availability>

    suspend fun checkAvailability(personalTrainerId: String, month: String): Result<CheckAvailability>
}
