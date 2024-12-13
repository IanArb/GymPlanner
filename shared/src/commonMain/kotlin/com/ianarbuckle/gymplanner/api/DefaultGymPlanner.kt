package com.ianarbuckle.gymplanner.api

import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.clients.ClientsRepository
import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.profile.domain.Profile
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.REMEMBER_ME_KEY
import com.ianarbuckle.gymplanner.storage.USER_ID
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DefaultGymPlanner(
    private val clientsRepository: ClientsRepository,
    private val fitnessClassRepository: FitnessClassRepository,
    private val faultReportingRepository: FaultReportingRepository,
    private val personalTrainersRepository: PersonalTrainersRepository,
    private val gymLocationsRepository: GymLocationsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val bookingRepository: BookingRepository,
    private val profileRepository: ProfileRepository,
    private val availabilityRepository: AvailabilityRepository,
    private val dataStoreRepository: DataStoreRepository,
    ) : GymPlanner {

    override suspend fun fetchAllClients(): Result<ImmutableList<Client>> {
        return clientsRepository.fetchClients()
    }

    override suspend fun saveClient(client: Client): Result<Client> {
        return clientsRepository.saveClient(client = client)
    }

    override suspend fun findClientById(id: String): Result<Client> {
        return clientsRepository.findClient(id)
    }

    override suspend fun deleteClient(id: String): Result<Unit> {
        return clientsRepository.deleteClient(id)
    }

    override suspend fun fetchTodaysFitnessClasses(): Result<ImmutableList<FitnessClass>> {
        val currentMoment: Instant = Clock.System.now()
        val datetimeInSystemZone: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeek = datetimeInSystemZone.dayOfWeek

        when (dayOfWeek) {
            DayOfWeek.MONDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "MONDAY")
            }
            DayOfWeek.TUESDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "TUESDAY")
            }
            DayOfWeek.WEDNESDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "WEDNESDAY")
            }
            DayOfWeek.THURSDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "THURSDAY")
            }
            DayOfWeek.FRIDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "FRIDAY")
            }
            DayOfWeek.SATURDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "SATURDAY")
            }
            DayOfWeek.SUNDAY -> {
                return fetchClassesByDayOfWeek(dayOfWeek = "SUNDAY")
            }
            else -> {
                return Result.failure(exception = Exception("No classes found"))
            }
        }
    }

    private suspend fun fetchClassesByDayOfWeek(dayOfWeek: String): Result<ImmutableList<FitnessClass>> {
        return fitnessClassRepository.fetchFitnessClasses(dayOfWeek)
    }

    override suspend fun fetchFitnessClasses(dayOfWeek: String): Result<ImmutableList<FitnessClass>> {
        return fitnessClassRepository.fetchFitnessClasses(dayOfWeek)
    }

    override suspend fun submitFault(fault: FaultReport): Result<FaultReport> {
        return faultReportingRepository.saveFaultReport(fault)
    }

    override suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<ImmutableList<PersonalTrainer>> {
        return personalTrainersRepository.fetchPersonalTrainers(gymLocation)
    }

    override suspend fun fetchGymLocations(): Result<ImmutableList<GymLocations>> {
        return gymLocationsRepository.fetchGymLocations()
    }

    override suspend fun login(login: Login): Result<LoginResponse> {
        return authenticationRepository.login(login)
    }

    override suspend fun saveAuthToken(token: String) {
        return dataStoreRepository.saveData(key = AUTH_TOKEN_KEY, value = token)
    }

    override suspend fun saveRememberMe(rememberMe: Boolean) {
        return dataStoreRepository.saveData(key = REMEMBER_ME_KEY, value = rememberMe)
    }

    override suspend fun fetchAuthToken(): String {
        return dataStoreRepository.getStringData(AUTH_TOKEN_KEY) ?: ""
    }

    override suspend fun saveUserId(userId: String) {
        dataStoreRepository.saveData(key = USER_ID, value = userId)
    }

    override suspend fun fetchUserId(): String {
        return dataStoreRepository.getStringData(USER_ID) ?: ""
    }

    override suspend fun fetchRememberMe(): Boolean {
        return dataStoreRepository.getBooleanData(REMEMBER_ME_KEY) ?: false
    }

    override suspend fun fetchProfile(userId: String): Result<Profile> {
        return profileRepository.fetchProfile(userId)
    }

    override suspend fun saveBooking(booking: Booking): Result<BookingResponse> {
        return bookingRepository.saveBooking(booking)
    }

    override suspend fun fetchBookingsByUserId(userId: String): Result<ImmutableList<BookingResponse>> {
        return bookingRepository.findBookingsByUserId(userId)
    }

    override suspend fun fetchAvailability(
        personalTrainerId: String,
        month: String
    ): Result<Availability> {
        return availabilityRepository.getAvailability(personalTrainerId, month)
    }

    override suspend fun checkAvailability(
        personalTrainerId: String,
        month: String
    ): Result<CheckAvailability> {
        return availabilityRepository.checkAvailability(
            personalTrainerId = personalTrainerId,
            month = month,
        )
    }
}