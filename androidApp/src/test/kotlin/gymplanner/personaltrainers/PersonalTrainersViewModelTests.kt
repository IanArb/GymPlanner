package gymplanner.personaltrainers

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersUiState
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class PersonalTrainersViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher
    )

    private val gymPlanner: GymPlanner = mockk()
    private val viewModel: PersonalTrainersViewModel = PersonalTrainersViewModel(gymPlanner, dispatcherProvider)

    @Test
    fun `fetchPersonalTrainers should update uiState to Success when API call succeeds`() = runTest {
        // Arrange
        val personalTrainers = persistentListOf(mockk<PersonalTrainer>())
        coEvery { gymPlanner.fetchPersonalTrainers(GymLocation.CLONTARF) } returns Result.success(personalTrainers)

        // Act
        viewModel.fetchPersonalTrainers(GymLocation.CLONTARF)

        // Assert
        viewModel.uiState.test {
            assertEquals(PersonalTrainersUiState.Loading, awaitItem())
            val successState = awaitItem() as PersonalTrainersUiState.Success
            assertEquals(personalTrainers, successState.personalTrainers)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchPersonalTrainers should update uiState to Failure when API call fails`() = runTest {
        // Arrange
        coEvery { gymPlanner.fetchPersonalTrainers(GymLocation.CLONTARF) } returns Result.failure(Exception("Fetch failed"))

        // Act
        viewModel.fetchPersonalTrainers(GymLocation.CLONTARF)

        // Assert
        viewModel.uiState.test {
            assertEquals(PersonalTrainersUiState.Loading, awaitItem())
            assertEquals(PersonalTrainersUiState.Failure, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}