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

    // ========== Fetch Facilities Status Tests ==========

    @Test
    fun `getFacilitiesStatus with multiple facilities returns success with list`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isSuccess, "Result should be successful")
        assertEquals(DomainFacilityLists.multipleStatuses, result.getOrNull())
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
        val facilities = result.getOrNull()
        assertNotNull(facilities)
        assertEquals(3, facilities.size)
    }

    @Test
    fun `getFacilitiesStatus with single facility returns list with one item`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.singleStatus

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainFacilityLists.singleStatus, result.getOrNull())
        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `getFacilitiesStatus with no facilities returns empty list`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.emptyList

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(DomainFacilityLists.emptyList, result.getOrNull())
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getFacilitiesStatus with network error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.networkError

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isFailure, "Result should be failure")
        assertEquals(Exceptions.networkError, result.exceptionOrNull())
    }

    @Test
    fun `getFacilitiesStatus with server error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.serverError

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.serverError, result.exceptionOrNull())
    }

    @Test
    fun `getFacilitiesStatus with unauthorized error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.unauthorized

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.unauthorized, result.exceptionOrNull())
    }

    // ========== Mapping Tests ==========

    @Test
    fun `getFacilitiesStatus maps DTOs to domain models correctly`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        val facilities = result.getOrNull()
        assertNotNull(facilities)
        assertEquals("facility-001", facilities[0].id)
        assertEquals("Treadmill", facilities[0].machineName)
        assertEquals(1, facilities[0].machineNumber)
        assertEquals(GymLocation.CLONTARF, facilities[0].gymLocation)
        assertEquals(Location.MAIN_GYM_FLOOR, facilities[0].location)
        assertEquals(FaultType.OTHER, facilities[0].faultType)
        assertEquals(MachineStatus.OPERATIONAL, facilities[0].status)
    }

    @Test
    fun `getFacilitiesStatus maps all machine statuses correctly`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        val facilities = result.getOrNull()
        assertNotNull(facilities)
        assertEquals(MachineStatus.OPERATIONAL, facilities[0].status)
        assertEquals(MachineStatus.OUT_OF_ORDER, facilities[1].status)
        assertEquals(MachineStatus.UNDER_MAINTENANCE, facilities[2].status)
    }

    @Test
    fun `getFacilitiesStatus maps all fault types correctly`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        val facilities = result.getOrNull()
        assertNotNull(facilities)
        assertEquals(FaultType.OTHER, facilities[0].faultType)
        assertEquals(FaultType.MECHANICAL, facilities[1].faultType)
        assertEquals(FaultType.ELECTRICAL, facilities[2].faultType)
    }

    @Test
    fun `getFacilitiesStatus preserves facility order`() = runTest {
        // Given
        fakeRemoteDataSource.facilitiesResponse = FacilityLists.multipleStatuses

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        val facilities = result.getOrNull()
        assertNotNull(facilities)
        assertEquals(FacilityStatuses.treadmillOperational, facilities[0])
        assertEquals(FacilityStatuses.rowerOutOfOrder, facilities[1])
        assertEquals(FacilityStatuses.ellipticalUnderMaintenance, facilities[2])
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
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(3, result1.getOrNull()?.size)
        assertEquals(1, result2.getOrNull()?.size)
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
        val facilities = result.getOrNull()
        assertNotNull(facilities)
        facilities.forEach { facility ->
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
    fun `getFacilitiesStatus with timeout error returns failure`() = runTest {
        // Given
        fakeRemoteDataSource.shouldThrowException = true
        fakeRemoteDataSource.exception = Exceptions.timeout

        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isFailure)
        assertEquals(Exceptions.timeout, result.exceptionOrNull())
    }

    @Test
    fun `successful getFacilitiesStatus does not return failure`() = runTest {
        // When
        val result = repository.getFacilitiesStatus(GymLocations.clontarf)

        // Then
        assertTrue(result.isSuccess, "Should not return failure")
    }
}
