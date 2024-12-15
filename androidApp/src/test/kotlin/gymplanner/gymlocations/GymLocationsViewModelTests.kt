package gymplanner.gymlocations

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GymLocationsViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher
    )

    private val gymPlanner: GymPlanner = mockk()
    private val viewModel: GymLocationsViewModel = GymLocationsViewModel(gymPlanner, dispatcherProvider)

    @Test
    fun `fetchGymLocations should update uiState to Success when API call succeeds`() = runTest {
        // Arrange
        val gymLocations = persistentListOf(mockk<GymLocations>())
        coEvery { gymPlanner.fetchGymLocations() } returns Result.success(gymLocations)

        // Act
        viewModel.fetchGymLocations()

        // Assert
        viewModel.uiState.test {
            assertEquals(GymLocationsUiState.Loading, awaitItem())
            val successState = awaitItem() as GymLocationsUiState.Success
            assertEquals(gymLocations, successState.gymLocations)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchGymLocations should update uiState to Failure when API call fails`() = runTest {
        // Arrange
        coEvery { gymPlanner.fetchGymLocations() } returns Result.failure(Exception("Fetch failed"))

        // Act
        viewModel.fetchGymLocations()

        // Assert
        viewModel.uiState.test {
            assertEquals(GymLocationsUiState.Loading, awaitItem())
            assertEquals(GymLocationsUiState.Failure, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}