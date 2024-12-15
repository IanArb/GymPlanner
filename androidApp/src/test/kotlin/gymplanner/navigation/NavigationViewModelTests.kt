package gymplanner.navigation

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.navigation.NavigationViewModel
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
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

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher
    )

    private val gymPlanner: GymPlanner = mockk()

    @Test
    fun `init should update rememberMe to true when fetchRememberMe returns true`() = runTest {
        // Arrange
        coEvery { gymPlanner.fetchRememberMe() } returns true

        // Act
        val viewModel = NavigationViewModel(gymPlanner, dispatcherProvider)

        // Assert
        viewModel.rememberMe.test {
            assertEquals(true, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init should update rememberMe to false when fetchRememberMe returns false`() = runTest {
        // Arrange
        coEvery { gymPlanner.fetchRememberMe() } returns false

        // Act
        val viewModel = NavigationViewModel(gymPlanner, dispatcherProvider)

        // Assert
        viewModel.rememberMe.test {
            assertEquals(false, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}