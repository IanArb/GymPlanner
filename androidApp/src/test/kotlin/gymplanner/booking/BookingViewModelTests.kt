package gymplanner.booking

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.availability.AvailabilityUiState
import com.ianarbuckle.gymplanner.android.availability.AvailabilityViewModel
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.calendarMonth
import com.ianarbuckle.gymplanner.availability.AvailabilityRepository
import com.ianarbuckle.gymplanner.availability.domain.CheckAvailability
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookingViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
    )

    private val bookingRepository: BookingRepository = mockk()
    private val availabilityRepository: AvailabilityRepository = mockk()
    private val clock: Clock = mockk(relaxed = true) {
        every { now() } returns Instant.parse("2023-01-01T00:00:00Z")
    }
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true) {
        every { get<String>("personalTrainerId") } returns "trainer123"
        every { set("personalTrainerId", any<String>()) } returns Unit
    }

    private val viewModel: AvailabilityViewModel = AvailabilityViewModel(
        bookingRepository = bookingRepository,
        availabilityRepository = availabilityRepository,
        savedStateHandle = savedStateHandle,
        clock = clock,
        dispatcherProvider = dispatcherProvider,
    )

    @Test
    fun `init should update bookingState to AvailabilitySuccess when API calls succeed`() = runTest {
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
    fun `init should update bookingState to Failed when API calls fail`() = runTest {
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

    @Test
    fun `saveBooking should update bookingState to BookingSuccess when API call succeeds`() = runTest {
        // Arrange
        val booking = mockk<Booking>()
        val bookingResponse = mockk<BookingResponse>()
        coEvery { bookingRepository.saveBooking(booking) } returns Result.success(bookingResponse)
        coEvery { availabilityRepository.checkAvailability(any(), any()) } returns Result.success(
            CheckAvailability(
                personalTrainerId = "trainer123",
                isAvailable = true,
            ),
        )
        coEvery { availabilityRepository.getAvailability(any(), any()) } returns Result.success(mockk())

        // Act
        viewModel.saveBooking(booking)

        // Assert
        viewModel.availabilityUiState.test {
            assertEquals(AvailabilityUiState.Loading, awaitItem())
            assertEquals(AvailabilityUiState.BookingSuccess(bookingResponse), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveBooking should update bookingState to Failed when API call fails`() = runTest {
        // Arrange
        val booking = mockk<Booking>()
        coEvery { bookingRepository.saveBooking(booking) } returns Result.failure(Exception("Save failed"))
        coEvery { availabilityRepository.checkAvailability(any(), any()) } returns Result.success(
            CheckAvailability(
                personalTrainerId = "trainer123",
                isAvailable = true,
            ),
        )
        coEvery { availabilityRepository.getAvailability(any(), any()) } returns Result.success(mockk())

        // Act
        viewModel.saveBooking(booking)

        // Assert
        viewModel.availabilityUiState.test {
            assertEquals(AvailabilityUiState.Loading, awaitItem())
            assertEquals(AvailabilityUiState.Failed, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
