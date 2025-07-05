package gymplanner.reporting

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.reporting.data.FormFaultReportUiState
import com.ianarbuckle.gymplanner.android.reporting.data.ReportingViewModel
import com.ianarbuckle.gymplanner.faultreporting.FaultReportingRepository
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ReportingViewModelTests {

  @get:Rule val testCoroutineRule = TestCoroutineRule()

  private val reportingRepository = mockk<FaultReportingRepository>()
  private val viewModel: ReportingViewModel =
    ReportingViewModel(faultReportingRepository = reportingRepository)

  @Test
  fun `submitFault should update uiState to FormSuccess when API call succeeds`() = runTest {
    // Arrange
    val faultReport = mockk<FaultReport>()
    coEvery { reportingRepository.saveFaultReport(faultReport) } returns Result.success(faultReport)

    // Act
    viewModel.submitFault(faultReport)

    // Assert
    viewModel.uiState.test {
      assertEquals(FormFaultReportUiState.FormLoading, awaitItem())
      val successState = awaitItem() as FormFaultReportUiState.FormSuccess
      assertEquals(faultReport, successState.data)
      cancelAndIgnoreRemainingEvents()
    }
  }

  @Test
  fun `submitFault should update uiState to FormError when API call fails`() = runTest {
    // Arrange
    val faultReport = mockk<FaultReport>()
    coEvery { reportingRepository.saveFaultReport(faultReport) } returns
      Result.failure(Exception("Submission failed"))

    // Act
    viewModel.submitFault(faultReport)

    // Assert
    viewModel.uiState.test {
      assertEquals(FormFaultReportUiState.FormLoading, awaitItem())
      assertEquals(FormFaultReportUiState.FormError, awaitItem())
      cancelAndIgnoreRemainingEvents()
    }
  }
}
