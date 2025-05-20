package gymplanner.navigation

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.navigation.NavigationViewModel
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.REMEMBER_ME_KEY
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class NavigationViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dataStoreRepository = mockk<DataStoreRepository>()

    @Test
    fun `init should update rememberMe to true when fetchRememberMe returns true`() = runTest {
        // Arrange
        coEvery { dataStoreRepository.getBooleanData(REMEMBER_ME_KEY) } returns true

        // Act
        val viewModel = NavigationViewModel(dataStoreRepository)

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
        val viewModel = NavigationViewModel(dataStoreRepository)

        // Assert
        viewModel.rememberMe.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
