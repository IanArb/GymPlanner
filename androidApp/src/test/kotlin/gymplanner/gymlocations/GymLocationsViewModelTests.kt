package gymplanner.gymlocations

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.gymlocations.GymLocationsRepository
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

    @get:Rule val testCoroutineRule = TestCoroutineRule()

    private val gymLocationsRepository = mockk<GymLocationsRepository>()

    @Test
    fun `fetchGymLocations should update uiState to Success when API call succeeds`() = runTest {
        // Arrange
        val gymLocations = persistentListOf(mockk<GymLocations>())
        coEvery { gymLocationsRepository.fetchGymLocations() } returns Result.success(gymLocations)

        val viewModel = GymLocationsViewModel(gymLocationsRepository = gymLocationsRepository)

        // Act
        viewModel.fetchGymLocations()

        // Assert
        viewModel.uiState.test {
            val item = awaitItem()
            if (item is GymLocationsUiState.Loading) {
                val successState = awaitItem() as GymLocationsUiState.Success
                assertEquals(gymLocations, successState.gymLocations)
            } else {
                val successState = item as GymLocationsUiState.Success
                assertEquals(gymLocations, successState.gymLocations)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchGymLocations should update uiState to Failure when API call fails`() = runTest {
        // Arrange
        coEvery { gymLocationsRepository.fetchGymLocations() } returns
            Result.failure(Exception("Fetch failed"))

        val viewModel = GymLocationsViewModel(gymLocationsRepository = gymLocationsRepository)

        // Act
        viewModel.fetchGymLocations()

        // Assert
        viewModel.uiState.test {
            val item = awaitItem()
            if (item is GymLocationsUiState.Loading) {
                assertEquals(GymLocationsUiState.Failure, awaitItem())
            } else {
                assertEquals(GymLocationsUiState.Failure, item)
            }
            cancelAndIgnoreRemainingEvents()
        }
    }
}
