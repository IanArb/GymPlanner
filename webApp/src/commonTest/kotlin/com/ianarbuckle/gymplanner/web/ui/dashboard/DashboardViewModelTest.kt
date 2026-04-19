package com.ianarbuckle.gymplanner.web.ui.dashboard

import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.facilities.FacilitiesRepository
import com.ianarbuckle.gymplanner.facilities.domain.FacilityStatus
import com.ianarbuckle.gymplanner.facilities.dto.FaultType
import com.ianarbuckle.gymplanner.facilities.dto.Location
import com.ianarbuckle.gymplanner.facilities.dto.MachineStatus
import com.ianarbuckle.gymplanner.web.ui.data.DashboardUiState
import com.ianarbuckle.gymplanner.web.ui.data.DashboardViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class DashboardViewModelTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var fakeFacilitiesRepository: FakeDashboardFacilitiesRepository

    @BeforeTest
    fun setup() {
        fakeFacilitiesRepository = FakeDashboardFacilitiesRepository()

        startKoin {
            modules(
                module {
                    single<FacilitiesRepository> { fakeFacilitiesRepository }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() {
        fakeFacilitiesRepository.reset()
        stopKoin()
    }

    // ========== Initial State ==========

    @Test
    fun `initial uiState is Idle`() =
        testScope.runTest {
            val viewModel = DashboardViewModel(testScope)

            assertIs<DashboardUiState.Idle>(viewModel.uiState.value)
        }

    // ========== fetchFacilities - Success ==========

    @Test
    fun `fetchFacilities with multiple facilities sets uiState to Success`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(TestData.multipleFacilities)

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<DashboardUiState.Success>(viewModel.uiState.value)
        }

    @Test
    fun `fetchFacilities with multiple facilities returns correct list`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(TestData.multipleFacilities)

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Success
            assertEquals(TestData.multipleFacilities.size, state.facilities.size)
            assertEquals(TestData.multipleFacilities, state.facilities.toList())
        }

    @Test
    fun `fetchFacilities with single facility sets uiState to Success`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(listOf(TestData.treadmillOperational))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Success
            assertEquals(1, state.facilities.size)
        }

    @Test
    fun `fetchFacilities with empty list sets uiState to Success with empty facilities`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(emptyList())

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Success
            assertEquals(0, state.facilities.size)
        }

    @Test
    fun `fetchFacilities passes gymLocation to repository`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(emptyList())

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.ASTONQUAY)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(GymLocation.ASTONQUAY, fakeFacilitiesRepository.capturedGymLocation)
        }

    // ========== fetchFacilities - Error ==========

    @Test
    fun `fetchFacilities with network error sets uiState to Error`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.failure(Exception("Network unavailable"))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<DashboardUiState.Error>(viewModel.uiState.value)
        }

    @Test
    fun `fetchFacilities with network error sets correct error message`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.failure(Exception("Network unavailable"))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Error
            assertEquals("Network unavailable", state.message)
        }

    @Test
    fun `fetchFacilities with server error sets uiState to Error`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.failure(RuntimeException("Internal server error"))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<DashboardUiState.Error>(viewModel.uiState.value)
        }

    @Test
    fun `fetchFacilities with server error sets correct error message`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.failure(RuntimeException("Internal server error"))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Error
            assertEquals("Internal server error", state.message)
        }

    @Test
    fun `fetchFacilities with thrown exception sets uiState to Error`() =
        testScope.runTest {
            fakeFacilitiesRepository.shouldThrowException = true

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<DashboardUiState.Error>(viewModel.uiState.value)
        }

    @Test
    fun `fetchFacilities with failure and null message uses fallback message`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.failure(Exception())

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Error
            assertEquals("Failed to load facilities", state.message)
        }

    // ========== fetchFacilities - Repeated Calls ==========

    @Test
    fun `fetchFacilities called twice returns latest result`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(listOf(TestData.treadmillOperational))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            fakeFacilitiesRepository.result = Result.success(TestData.multipleFacilities)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.uiState.value as DashboardUiState.Success
            assertEquals(TestData.multipleFacilities.size, state.facilities.size)
        }

    @Test
    fun `fetchFacilities success followed by error sets uiState to Error`() =
        testScope.runTest {
            fakeFacilitiesRepository.result = Result.success(listOf(TestData.treadmillOperational))

            val viewModel = DashboardViewModel(testScope)
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            fakeFacilitiesRepository.result = Result.failure(Exception("Network unavailable"))
            viewModel.fetchFacilities(GymLocation.CLONTARF)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<DashboardUiState.Error>(viewModel.uiState.value)
        }
}

// ========== Fakes ==========

private class FakeDashboardFacilitiesRepository : FacilitiesRepository {
    var result: Result<List<FacilityStatus>> = Result.failure(RuntimeException("Not configured"))
    var shouldThrowException = false
    var capturedGymLocation: GymLocation? = null

    override suspend fun getFacilitiesStatus(gymLocation: GymLocation): Result<List<FacilityStatus>> {
        capturedGymLocation = gymLocation
        if (shouldThrowException) throw RuntimeException("Unexpected exception")
        return result
    }

    fun reset() {
        result = Result.failure(RuntimeException("Not configured"))
        shouldThrowException = false
        capturedGymLocation = null
    }
}

// ========== Test Data ==========

private object TestData {
    val treadmillOperational = FacilityStatus(
        id = "facility-001",
        machineName = "Treadmill",
        machineNumber = 1,
        gymLocation = GymLocation.CLONTARF,
        location = Location.MAIN_GYM_FLOOR,
        faultType = FaultType.OTHER,
        status = MachineStatus.OPERATIONAL,
    )

    val rowerOutOfOrder = FacilityStatus(
        id = "facility-002",
        machineName = "Rowing Machine",
        machineNumber = 2,
        gymLocation = GymLocation.CLONTARF,
        location = Location.BLUE_GYM_FLOOR,
        faultType = FaultType.MECHANICAL,
        status = MachineStatus.OUT_OF_ORDER,
    )

    val ellipticalUnderMaintenance = FacilityStatus(
        id = "facility-003",
        machineName = "Elliptical",
        machineNumber = 3,
        gymLocation = GymLocation.ASTONQUAY,
        location = Location.FREE_WEIGHTS_AREA,
        faultType = FaultType.ELECTRICAL,
        status = MachineStatus.UNDER_MAINTENANCE,
    )

    val multipleFacilities = listOf(rowerOutOfOrder, ellipticalUnderMaintenance, treadmillOperational)
}
