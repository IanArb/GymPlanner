package com.ianarbuckle.gymplanner.facilities

import com.ianarbuckle.gymplanner.facilities.FacilitiesTestDataProvider.DomainFacilityLists
import com.ianarbuckle.gymplanner.facilities.FacilitiesTestDataProvider.Exceptions
import com.ianarbuckle.gymplanner.facilities.FacilitiesTestDataProvider.FacilityLists
import com.ianarbuckle.gymplanner.facilities.FacilitiesTestDataProvider.FacilityStatuses
import com.ianarbuckle.gymplanner.facilities.FacilitiesTestDataProvider.GymLocations
import com.ianarbuckle.gymplanner.facilities.dto.FaultType
import com.ianarbuckle.gymplanner.facilities.dto.Location
import com.ianarbuckle.gymplanner.facilities.dto.MachineStatus
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class FacilitiesRepositoryTest {

    private lateinit var repository: FacilitiesRepository
    private lateinit var fakeRemoteDataSource: FakeFacilitiesRemoteDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeFacilitiesRemoteDataSource()
        repository = FakeFacilitiesRepository(fakeRemoteDataSource)
    }

    @AfterTest
    fun tearDown() {
        fakeRemoteDataSource.reset()
    }

    @Test
    fun `getFacilitiesStatus with multiple facilities returns list`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(DomainFacilityLists.multipleStatuses, result)
        assertEquals(1, fakeRemoteDataSource.findByLocationCalls.size)
    }

    @Test
    fun `getFacilitiesStatus calls remote data source with correct gym location`() = runTest {
        // When
        repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(1, fakeRemoteDataSource.findByLocationCalls.size)
        assertEquals(GymLocations.clontarf, fakeRemoteDataSource.findByLocationCalls[0])
    }

    @Test
    fun `getFacilitiesStatus with multiple facilities returns correct count`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(3, result.size)
    }

    @Test
    fun `getFacilitiesStatus with single facility returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.singleStatus

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(DomainFacilityLists.singleStatus, result)
        assertEquals(1, result.size)
    }

    @Test
    fun `getFacilitiesStatus with no facilities returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.emptyList

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(DomainFacilityLists.emptyList, result)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getFacilitiesStatus with network error throws exception`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.networkError

        // Then
        assertFailsWith<Exception> { repository.getFacilitiesStatus(GymLocations.clontarf) }
    }

    @Test
    fun `getFacilitiesStatus with server error throws exception`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.serverError

        // Then
        assertFailsWith<RuntimeException> { repository.getFacilitiesStatus(GymLocations.clontarf) }
    }

    @Test
    fun `getFacilitiesStatus with unauthorized error throws exception`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.unauthorized

        // Then
        assertFailsWith<RuntimeException> { repository.getFacilitiesStatus(GymLocations.clontarf) }
    }

    // ========== Mapping Tests ==========

    @Test
    fun `getFacilitiesStatus maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertNotNull(result)
        assertEquals("facility-001", result[0].id)
        assertEquals("Treadmill", result[0].machineName)
        assertEquals(1, result[0].machineNumber)
        assertEquals(GymLocation.CLONTARF, result[0].gymLocation)
        assertEquals(Location.MAIN_GYM_FLOOR, result[0].location)
        assertEquals(FaultType.OTHER, result[0].faultType)
        assertEquals(MachineStatus.OPERATIONAL, result[0].status)
    }

    @Test
    fun `getFacilitiesStatus maps all machine statuses correctly`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(MachineStatus.OPERATIONAL, result[0].status)
        assertEquals(MachineStatus.OUT_OF_ORDER, result[1].status)
        assertEquals(MachineStatus.UNDER_MAINTENANCE, result[2].status)
    }

    @Test
    fun `getFacilitiesStatus maps all fault types correctly`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(FaultType.OTHER, result[0].faultType)
        assertEquals(FaultType.MECHANICAL, result[1].faultType)
        assertEquals(FaultType.ELECTRICAL, result[2].faultType)
    }

    @Test
    fun `getFacilitiesStatus preserves facility order`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertEquals(FacilityStatuses.treadmillOperational, result[0])
        assertEquals(FacilityStatuses.rowerOutOfOrder, result[1])
        assertEquals(FacilityStatuses.ellipticalUnderMaintenance, result[2])
    }

    // ========== Gym Location Forwarding Tests ==========

    @Test
    fun `getFacilitiesStatus passes different gym locations to remote data source`() = runTest {
        // When
        repository.getFacilitiesStatus(GymLocations.clontarf)
        repository.getFacilitiesStatus(GymLocations.astonQuay)
        repository.getFacilitiesStatus(GymLocations.leopardstown)

        // Then
        assertEquals(3, fakeRemoteDataSource.findByLocationCalls.size)
        assertEquals(GymLocations.clontarf, fakeRemoteDataSource.findByLocationCalls[0])
        assertEquals(GymLocations.astonQuay, fakeRemoteDataSource.findByLocationCalls[1])
        assertEquals(GymLocations.leopardstown, fakeRemoteDataSource.findByLocationCalls[2])
    }

    @Test
    fun `multiple getFacilitiesStatus calls work independently`() = runTest {
        // When
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses
        val result1 = repository.getFacilitiesStatus(GymLocations.clontarf)

        fakeRemoteDataSource.facilitiesResponse = FacilityLists.singleStatus
        val result2 = repository.getFacilitiesStatus(GymLocations.astonQuay)

        // Then
        assertEquals(3, result1.size)
        assertEquals(1, result2.size)
        assertEquals(2, fakeRemoteDataSource.findByLocationCalls.size)
    }

    // ========== Edge Cases ==========

    @Test
    fun `getFacilitiesStatus response contains all facility fields`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        result.forEach { facility ->
            assertNotNull(facility.id)
            assertNotNull(facility.machineName)
            assertNotNull(facility.machineNumber)
            assertNotNull(facility.gymLocation)
            assertNotNull(facility.location)
            assertNotNull(facility.faultType)
            assertNotNull(facility.status)
        }
    }

    @Test
    fun `getFacilitiesStatus with timeout error throws exception`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.timeout

        // Then
        assertFailsWith<Exception> { repository.getFacilitiesStatus(GymLocations.clontarf) }
    }

    @Test
    fun `successful getFacilitiesStatus does not throw exception`() = runTest {
        // When
        var exception: Exception? = null
        try {
            repository.getFacilitiesStatus(GymLocations.clontarf)
        } catch (e: Exception) {
            exception = e
        }

        // Then
        assertEquals(null, exception, "Should not throw exception")
    }
}
