package com.ianarbuckle.gymplanner

import com.ianarbuckle.gymplanner.data.clients.ClientsRepository
import com.ianarbuckle.gymplanner.data.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.data.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.model.Client
import com.ianarbuckle.gymplanner.model.FaultReport
import com.ianarbuckle.gymplanner.model.FitnessClass
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
}