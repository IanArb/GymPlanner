package gymplanner.personaltrainers

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersUiState
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.personaltrainers.PersonalTrainersRepository
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

    @get:Rule val testCoroutineRule = TestCoroutineRule()

    private val personalTrainersRepository = mockk<PersonalTrainersRepository>()

    @Test
    fun `fetchPersonalTrainers should update uiState to Success when API call succeeds`() =
        runTest {
            // Arrange
            val personalTrainers = persistentListOf(mockk<PersonalTrainer>())
            coEvery {
                personalTrainersRepository.fetchPersonalTrainers(GymLocation.CLONTARF)
            } returns Result.success(personalTrainers)

            val viewModel =
                PersonalTrainersViewModel(
                    personalTrainersRepository = personalTrainersRepository,
                    gymLocation = GymLocation.CLONTARF,
                )

            viewModel.fetchPersonalTrainers()

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
        coEvery { personalTrainersRepository.fetchPersonalTrainers(GymLocation.CLONTARF) } returns
            Result.failure(Exception("Fetch failed"))

        val viewModel =
            PersonalTrainersViewModel(
                personalTrainersRepository = personalTrainersRepository,
                gymLocation = GymLocation.CLONTARF,
            )

        viewModel.fetchPersonalTrainers()

        // Assert
        viewModel.uiState.test {
            assertEquals(PersonalTrainersUiState.Loading, awaitItem())
            assertEquals(PersonalTrainersUiState.Failure, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
