package gymplanner.login

import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.login.data.LoginState
import com.ianarbuckle.gymplanner.android.login.data.LoginViewModel
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LoginViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher
    )

    private val gymPlanner: GymPlanner = mockk()
    private val viewModel: LoginViewModel = LoginViewModel(gymPlanner, dispatcherProvider)

    @Test
    fun `login should update loginState to Success when API call succeeds`() = runTest {
        // Arrange
        val login = Login("username", "password")
        val loginResponse = LoginResponse("token", "userId",500L)
        coEvery { gymPlanner.login(login) } returns Result.success(loginResponse)
        coEvery { gymPlanner.saveAuthToken(loginResponse.token) } returns Unit
        coEvery { gymPlanner.saveUserId(loginResponse.userId) } returns Unit

        // Act
        viewModel.login(login)

        // Assert
        viewModel.loginState.test {
            assertEquals(LoginState.Loading, awaitItem())
            assertEquals(LoginState.Success(loginResponse), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login should update loginState to Error when API call fails`() = runTest {
        // Arrange
        val login = Login("username", "password")
        coEvery { gymPlanner.login(login) } returns Result.failure(Exception("Login failed"))

        // Act
        viewModel.login(login)

        // Assert
        viewModel.loginState.test {
            assertEquals(LoginState.Loading, awaitItem())
            assertEquals(LoginState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `persistRememberMe should call saveRememberMe on gymPlanner`() = runTest {
        // Arrange
        val rememberMe = true
        coEvery { gymPlanner.saveRememberMe(rememberMe) } returns Unit

        // Act
        viewModel.persistRememberMe(rememberMe)

        testCoroutineRule.testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        // No state to test, just verify the method call
        coVerify { gymPlanner.saveRememberMe(rememberMe) }
    }
}