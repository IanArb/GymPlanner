package gymplanner.booking

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.booking.BookingUiState
import com.ianarbuckle.gymplanner.android.booking.BookingViewModel
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingDetailsData
import com.ianarbuckle.gymplanner.booking.BookingRepository
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalTime
import okio.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookingViewModelTests {

  @get:Rule val testCoroutineRule = TestCoroutineRule()

  private val bookingRepository: BookingRepository = mockk()

  private val dataStoreRepository: DataStoreRepository = mockk()

  private val viewModel: BookingViewModel =
    BookingViewModel(
      bookingRepository = bookingRepository,
      dataStoreRepository = dataStoreRepository,
    )

  val bookingDetailsData =
    mockk<BookingDetailsData> {
      every { timeSlotId } returns "112"
      every { selectedDate } returns "2023-01-01"
      every { selectedTimeSlot } returns LocalTime.parse("10:00:00")
      every { personalTrainerName } returns "John Doe"
      every { personalTrainerAvatarUrl } returns "https://example.com/avatar.jpg"
      every { personalTrainerId } returns "1234"
      every { location } returns "CLONTARF"
    }

  @Before
  fun setup() {
    // Set up any necessary initial state or dependencies here
    coEvery { dataStoreRepository.getStringData(USER_ID) } returns "112"
  }

  @Test
  fun `saveBooking should update bookingUiState to Success when API call succeeds`() = runTest {
    // Arrange
    val expectedResponse = mockk<BookingResponse>()

    // Mock the API call to return a successful response
    coEvery { bookingRepository.saveBooking(any()) } returns Result.success(expectedResponse)

    // Act
    viewModel.saveBooking(bookingDetailsData)

    // Assert
    viewModel.bookingUiState.test {
      assertEquals(BookingUiState.Loading, awaitItem())
      assertEquals(BookingUiState.Success(expectedResponse), awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `saveBooking should update bookingUiState to Failed when API call fails`() = runTest {
    // Arrange
    coEvery { bookingRepository.saveBooking(any()) } returns Result.failure(IOException())

    // Act
    viewModel.saveBooking(bookingDetailsData)

    // Assert
    viewModel.bookingUiState.test {
      assertEquals(BookingUiState.Loading, awaitItem())
      assertEquals(BookingUiState.Failed, awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }
}
