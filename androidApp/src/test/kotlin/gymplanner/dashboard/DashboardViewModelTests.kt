package gymplanner.dashboard

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.profile.domain.Profile
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher
    )

    private val gymPlanner: GymPlanner = mockk()
    private val viewModel: DashboardViewModel = DashboardViewModel(gymPlanner, dispatcherProvider)

    @Test
    fun `fetchFitnessClasses should update uiState to Success when API calls succeed`() = runTest {
        // Arrange
        val userId = "user123"
        val profile = mockk<Profile>()
        val classes = persistentListOf(mockk<FitnessClass>())

        coEvery { gymPlanner.fetchUserId() } returns userId
        coEvery { gymPlanner.fetchProfile(userId) } returns Result.success(profile)
        coEvery { gymPlanner.fetchTodaysFitnessClasses() } returns Result.success(classes)

        // Act

        viewModel.fetchFitnessClasses()

        // Assert
        viewModel.uiState.test {
            assertEquals(DashboardUiState.Loading, awaitItem())
            val successState = awaitItem() as DashboardUiState.Success
            assertEquals(profile, successState.profile)
            assertEquals(classes, successState.items)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchFitnessClasses should update uiState to Failure when API calls fail`() = runTest {
        // Arrange
        val userId = "user123"

        coEvery { gymPlanner.fetchUserId() } returns userId
        coEvery { gymPlanner.fetchProfile(userId) } returns Result.failure(Exception("Profile fetch failed"))
        coEvery { gymPlanner.fetchTodaysFitnessClasses() } returns Result.failure(Exception("Classes fetch failed"))

        // Act
        viewModel.fetchFitnessClasses()

        // Assert
        viewModel.uiState.test {
            assertEquals(DashboardUiState.Loading, awaitItem())
            assertEquals(DashboardUiState.Failure, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}