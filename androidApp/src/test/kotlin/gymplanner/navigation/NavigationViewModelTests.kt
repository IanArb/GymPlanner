package gymplanner.navigation

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.LoginScreen
import com.ianarbuckle.gymplanner.android.navigation.NavigationEvent
import com.ianarbuckle.gymplanner.android.navigation.NavigationViewModel
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.navigation.Root
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.REMEMBER_ME_KEY
import gymplanner.navigation.TestDataFactory.createAvailabilityScreen
import gymplanner.navigation.TestDataFactory.createBookingScreen
import gymplanner.navigation.TestDataFactory.createConversationScreen
import gymplanner.navigation.TestDataFactory.createNavigateToAvailabilityEvent
import gymplanner.navigation.TestDataFactory.createNavigateToBookingEvent
import gymplanner.navigation.TestDataFactory.createNavigateToChatEvent
import gymplanner.navigation.TestDataFactory.createNavigateToPersonalTrainersDetailsEvent
import gymplanner.navigation.TestDataFactory.createNavigateToPersonalTrainersEvent
import gymplanner.navigation.TestDataFactory.createPersonalTrainersDetailScreen
import gymplanner.navigation.TestDataFactory.createPersonalTrainersScreen
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationViewModelTests {

    @get:Rule val testCoroutineRule = TestCoroutineRule()

    private val dataStoreRepository = mockk<DataStoreRepository>()

    @Before
    fun setup() {
        coEvery { dataStoreRepository.getBooleanData(REMEMBER_ME_KEY) } returns true
    }

    @Test
    fun `init should update rememberMe to true when fetchRememberMe returns true`() = runTest {
        // Arrange
        coEvery { dataStoreRepository.getBooleanData(REMEMBER_ME_KEY) } returns true

        // Act
        val viewModel = createViewModel()

        // Assert
        viewModel.rememberMe.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init should update rememberMe to false when fetchRememberMe returns false`() = runTest {
        // Arrange
        coEvery { dataStoreRepository.getBooleanData(REMEMBER_ME_KEY) } returns false

        // Act
        val viewModel = createViewModel()

        // Assert
        viewModel.rememberMe.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onNavigate with NavigateToDashboard should add DashboardScreen to back stack`() = runTest {
        // Act
        val viewModel = createViewModel()

        viewModel.onNavigate(NavigationEvent.NavigateToDashboard)

        // Assert
        assertEquals(DashboardScreen, viewModel.navigationBackStack.last())
    }

    @Test
    fun `onNavigate with NavigateBack should remove last item from back stack`() = runTest {
        // Act
        val viewModel = createViewModel()
        viewModel.navigationBackStack.add(DashboardScreen)
        viewModel.onNavigate(NavigationEvent.NavigateBack)

        // Assert
        assertEquals(Root, viewModel.navigationBackStack.last())
    }

    @Test
    fun `onNavigate with NavigateToAvailability should add AvailabilityScreen to back stack`() =
        runTest {
            // Act
            val viewModel = createViewModel()
            val event = createNavigateToAvailabilityEvent()
            viewModel.onNavigate(event)

            assertEquals(createAvailabilityScreen(), viewModel.navigationBackStack.last())
        }

    @Test
    fun `onNavigate with NavigateToBooking should add BookingScreen to back stack`() = runTest {
        // Act
        val viewModel = createViewModel()
        val event = createNavigateToBookingEvent()
        viewModel.onNavigate(event)

        // Assert
        assertEquals(createBookingScreen(), viewModel.navigationBackStack.last())
    }

    @Test
    fun `onNavigate with NavigateToChat should add ConversationScreen to back stack`() = runTest {
        // Act
        val viewModel = createViewModel()
        val event = createNavigateToChatEvent()
        viewModel.onNavigate(event)

        // Assert
        assertEquals(createConversationScreen(), viewModel.navigationBackStack.last())
    }

    @Test
    fun `onNavigate with NavigateToGymLocations should add GymLocationsScreen to back stack`() =
        runTest {
            // Act
            val viewModel = createViewModel()
            viewModel.onNavigate(NavigationEvent.NavigateToGymLocations)

            // Assert
            assertEquals(GymLocationsScreen, viewModel.navigationBackStack.last())
        }

    @Test
    fun `onNavigate with NavigateToLogin should add LoginScreen to back stack`() = runTest {
        // Act
        val viewModel = createViewModel()
        viewModel.onNavigate(NavigationEvent.NavigateToLogin)

        // Assert
        assertEquals(LoginScreen, viewModel.navigationBackStack.last())
    }

    @Test
    fun `onNavigate with NavigateToPersonalTrainers should add PersonalTrainersScreen to back stack`() =
        runTest {
            // Act
            val viewModel = createViewModel()
            val event = createNavigateToPersonalTrainersEvent()
            viewModel.onNavigate(event)

            // Assert
            assertEquals(createPersonalTrainersScreen(), viewModel.navigationBackStack.last())
        }

    @Test
    fun `onNavigate with NavigateToReportMachineBroken should add ReportMachineBroken to back stack`() =
        runTest {
            // Act
            val viewModel = createViewModel()
            viewModel.onNavigate(NavigationEvent.NavigateToReportMachineBroken)

            // Assert
            assertEquals(ReportMachineBroken, viewModel.navigationBackStack.last())
        }

    @Test
    fun `onNavigate with NavigateToPersonalTrainersDetails should add PersonalTrainersDetailScreen to back stack`() =
        runTest {
            // Act
            val viewModel = createViewModel()
            val event = createNavigateToPersonalTrainersDetailsEvent()
            viewModel.onNavigate(event)

            // Assert
            assertEquals(createPersonalTrainersDetailScreen(), viewModel.navigationBackStack.last())
        }

    @Test
    fun `onNavigate with NavigationBottomBar should add destination to back stack`() = runTest {
        // Act
        val viewModel = createViewModel()
        val event = NavigationEvent.NavigationBottomBar(DashboardScreen)
        viewModel.onNavigate(event)

        // Assert
        assertEquals(DashboardScreen, viewModel.navigationBackStack.last())
    }

    private fun createViewModel(): NavigationViewModel = NavigationViewModel(dataStoreRepository)
}
