package com.ianarbuckle.gymplanner.api

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
    ) : GymPlanner {

    override suspend fun fetchAllClients(): Result<List<Client>> {
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

    override suspend fun fetchTodaysFitnessClasses(): Result<List<FitnessClass>> {
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

    private suspend fun fetchClassesByDayOfWeek(dayOfWeek: String): Result<List<FitnessClass>> {
        return fitnessClassRepository.fetchFitnessClasses(dayOfWeek)
    }

    override suspend fun fetchFitnessClasses(dayOfWeek: String): Result<List<FitnessClass>> {
        return fitnessClassRepository.fetchFitnessClasses(dayOfWeek)
    }

    override suspend fun submitFault(fault: FaultReport): Result<FaultReport> {
        return faultReportingRepository.saveFaultReport(fault)
    }

    override suspend fun fetchPersonalTrainers(gymLocation: GymLocation): Result<List<PersonalTrainer>> {
        return personalTrainersRepository.fetchPersonalTrainers(gymLocation)
    }

    override suspend fun fetchGymLocations(): Result<List<GymLocations>> {
        return gymLocationsRepository.fetchGymLocations()
    }
}