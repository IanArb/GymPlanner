package gymplanner.availability

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.availability.AvailabilityUiState
import com.ianarbuckle.gymplanner.android.availability.AvailabilityViewModel
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.calendarMonth
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class AvailabilityViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val availabilityRepository: AvailabilityRepository = mockk()

    @OptIn(ExperimentalTime::class)
    private val clock: Clock = mockk(relaxed = true) {
        every { now() } returns Instant.parse("2023-01-01T00:00:00Z")
    }
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true) {
        every { get<String>("personalTrainerId") } returns "trainer123"
        every { set("personalTrainerId", any<String>()) } returns Unit
    }

    private val viewModel: AvailabilityViewModel = AvailabilityViewModel(
        availabilityRepository = availabilityRepository,
        savedStateHandle = savedStateHandle,
        clock = clock,
    )

    @Test
    fun `init should update state to AvailabilitySuccess when API calls succeed`() = runTest {
        // Arrange
        val personalTrainerId = "trainer123"
        val currentDateTime = Instant.parse("2023-01-01T00:00:00Z").toLocalDateTime(TimeZone.UTC)
        coEvery { availabilityRepository.checkAvailability(personalTrainerId, currentDateTime.calendarMonth()) } returns Result.success(
            CheckAvailability(
                personalTrainerId = personalTrainerId,
                isAvailable = true,
            ),
        )
        coEvery { availabilityRepository.getAvailability(personalTrainerId, currentDateTime.calendarMonth()) } returns Result.success(
            DataProvider.availability(),
        )

        viewModel.fetchAvailability()

        // Act
        viewModel.availabilityUiState.test {
            // Assert
            assertEquals(AvailabilityUiState.Idle, awaitItem())
            assertEquals(AvailabilityUiState.Loading, awaitItem())
            val successState = awaitItem() as AvailabilityUiState.AvailabilitySuccess
            assertEquals(true, successState.isPersonalTrainerAvailable)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init should update state to Failed when API calls fail`() = runTest {
        // Arrange
        val personalTrainerId = "trainer123"
        val currentDateTime = Instant.parse("2023-01-01T00:00:00Z").toLocalDateTime(TimeZone.UTC)
        every { savedStateHandle.get<String>("personalTrainerId") } returns personalTrainerId
        coEvery { clock.now() } returns Instant.parse("2023-01-01T00:00:00Z")
        coEvery { availabilityRepository.checkAvailability(personalTrainerId, currentDateTime.calendarMonth()) } returns Result.failure(Exception("Check failed"))
        coEvery { availabilityRepository.getAvailability(personalTrainerId, currentDateTime.calendarMonth()) } returns Result.failure(Exception("Fetch failed"))

        viewModel.fetchAvailability()

        // Act
        viewModel.availabilityUiState.test {
            // Assert
            assertEquals(AvailabilityUiState.Idle, awaitItem())
            assertEquals(AvailabilityUiState.Loading, awaitItem())
            assertEquals(AvailabilityUiState.Failed, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
