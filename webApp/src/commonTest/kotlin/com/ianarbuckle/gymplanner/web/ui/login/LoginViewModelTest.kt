package com.ianarbuckle.gymplanner.web.ui.login

import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse
import com.ianarbuckle.gymplanner.authentication.domain.Register
import com.ianarbuckle.gymplanner.authentication.domain.RegisterResponse
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest

class LoginViewModelTest : KoinTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var fakeAuthRepository: FakeAuthenticationRepository
    private lateinit var fakeDataStoreRepository: FakeWebDataStoreRepository

    @BeforeTest
    fun setup() {
        fakeAuthRepository = FakeAuthenticationRepository()
        fakeDataStoreRepository = FakeWebDataStoreRepository()

        startKoin {
            modules(
                module {
                    single<AuthenticationRepository> { fakeAuthRepository }
                    single<DataStoreRepository> { fakeDataStoreRepository }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    // ========== Initial State ==========

    @Test
    fun `initial uiState is Idle`() =
        testScope.runTest {
            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<LoginUiState.Idle>(viewModel.uiState.value)
        }

    @Test
    fun `isCheckingAuth is true before auth check completes`() =
        testScope.runTest {
            val viewModel = LoginViewModel(testScope)

            assertTrue(viewModel.isCheckingAuth.value)
        }

    @Test
    fun `isCheckingAuth is false after auth check completes`() =
        testScope.runTest {
            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isCheckingAuth.value)
        }

    // ========== Auth Check on Init ==========

    @Test
    fun `isAuthenticated is false when no token in storage`() =
        testScope.runTest {
            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isAuthenticated.value)
        }

    @Test
    fun `isAuthenticated is true when valid token exists in storage`() =
        testScope.runTest {
            fakeDataStoreRepository.storedToken = "existing-valid-token"

            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            assertTrue(viewModel.isAuthenticated.value)
        }

    @Test
    fun `isAuthenticated is false when stored token is blank`() =
        testScope.runTest {
            fakeDataStoreRepository.storedToken = ""

            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isAuthenticated.value)
        }

    // ========== Login ==========

    @Test
    fun `login success sets uiState to Success and isAuthenticated to true`() =
        testScope.runTest {
            fakeAuthRepository.loginResult =
                Result.success(
                    LoginResponse(userId = "user-123", token = "jwt-token", expiration = 3600L)
                )

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "password123")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<LoginUiState.Success>(viewModel.uiState.value)
            assertEquals("jwt-token", (viewModel.uiState.value as LoginUiState.Success).token)
            assertTrue(viewModel.isAuthenticated.value)
        }

    @Test
    fun `login success saves token and userId to storage`() =
        testScope.runTest {
            fakeAuthRepository.loginResult =
                Result.success(
                    LoginResponse(userId = "user-123", token = "jwt-token", expiration = 3600L)
                )

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "password123")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals("jwt-token", fakeDataStoreRepository.getStringData(AUTH_TOKEN_KEY))
            assertEquals("user-123", fakeDataStoreRepository.getStringData(USER_ID))
        }

    @Test
    fun `login failure sets uiState to Error`() =
        testScope.runTest {
            fakeAuthRepository.loginResult = Result.failure(RuntimeException("Invalid credentials"))

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "wrongpassword")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<LoginUiState.Error>(viewModel.uiState.value)
            assertEquals(
                "Invalid credentials",
                (viewModel.uiState.value as LoginUiState.Error).message,
            )
        }

    @Test
    fun `login failure does not set isAuthenticated to true`() =
        testScope.runTest {
            fakeAuthRepository.loginResult = Result.failure(RuntimeException("Invalid credentials"))

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "wrongpassword")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isAuthenticated.value)
        }

    // ========== Logout ==========

    @Test
    fun `logout sets isAuthenticated to false`() =
        testScope.runTest {
            fakeDataStoreRepository.storedToken = "existing-token"

            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.dispatchAction(LoginAction.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isAuthenticated.value)
        }

    @Test
    fun `logout resets uiState to Idle`() =
        testScope.runTest {
            fakeAuthRepository.loginResult =
                Result.success(
                    LoginResponse(userId = "user-123", token = "jwt-token", expiration = 3600L)
                )

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "password123")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.dispatchAction(LoginAction.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<LoginUiState.Idle>(viewModel.uiState.value)
        }

    @Test
    fun `logout clears storage`() =
        testScope.runTest {
            fakeDataStoreRepository.storedToken = "existing-token"

            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.dispatchAction(LoginAction.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(1, fakeDataStoreRepository.clearAllDataCalls)
        }

    // ========== Login Storage Error ==========

    @Test
    fun `login sets uiState to Error when saving token fails`() =
        testScope.runTest {
            fakeAuthRepository.loginResult =
                Result.success(
                    LoginResponse(userId = "user-123", token = "jwt-token", expiration = 3600L)
                )
            fakeDataStoreRepository.shouldThrowOnSaveString = true

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "password123")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<LoginUiState.Error>(viewModel.uiState.value)
            assertEquals(
                "Failed to save authentication data",
                (viewModel.uiState.value as LoginUiState.Error).message,
            )
        }

    @Test
    fun `login does not set isAuthenticated to true when saving token fails`() =
        testScope.runTest {
            fakeAuthRepository.loginResult =
                Result.success(
                    LoginResponse(userId = "user-123", token = "jwt-token", expiration = 3600L)
                )
            fakeDataStoreRepository.shouldThrowOnSaveString = true

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "password123")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isAuthenticated.value)
        }

    @Test
    fun `login attempts clearAllData when saving token fails`() =
        testScope.runTest {
            fakeAuthRepository.loginResult =
                Result.success(
                    LoginResponse(userId = "user-123", token = "jwt-token", expiration = 3600L)
                )
            fakeDataStoreRepository.shouldThrowOnSaveString = true

            val viewModel = LoginViewModel(testScope)
            viewModel.dispatchAction(
                LoginAction.Login(username = "testuser", password = "password123")
            )
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(1, fakeDataStoreRepository.clearAllDataCalls)
        }

    // ========== Logout Storage Error ==========

    @Test
    fun `logout sets isAuthenticated to false even when clearAllData throws`() =
        testScope.runTest {
            fakeDataStoreRepository.storedToken = "existing-token"
            fakeDataStoreRepository.shouldThrowOnClearAllData = true

            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.dispatchAction(LoginAction.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            assertFalse(viewModel.isAuthenticated.value)
        }

    @Test
    fun `logout resets uiState to Idle even when clearAllData throws`() =
        testScope.runTest {
            fakeDataStoreRepository.storedToken = "existing-token"
            fakeDataStoreRepository.shouldThrowOnClearAllData = true

            val viewModel = LoginViewModel(testScope)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.dispatchAction(LoginAction.Logout)
            testDispatcher.scheduler.advanceUntilIdle()

            assertIs<LoginUiState.Idle>(viewModel.uiState.value)
        }

}

// ========== Fakes ==========

private class FakeAuthenticationRepository : AuthenticationRepository {
    var loginResult: Result<LoginResponse> = Result.failure(RuntimeException("Not configured"))

    override suspend fun login(login: Login): Result<LoginResponse> = loginResult

    override suspend fun register(register: Register): Result<RegisterResponse> =
        Result.failure(NotImplementedError())
}

private class FakeWebDataStoreRepository : DataStoreRepository {
    var storedToken: String? = null
    var shouldThrowOnSaveString = false
    var shouldThrowOnClearAllData = false
    private val stringStorage = mutableMapOf<Preferences.Key<String>, String>()
    private val booleanStorage = mutableMapOf<Preferences.Key<Boolean>, Boolean>()
    var clearAllDataCalls = 0

    init {
        storedToken?.let { stringStorage[AUTH_TOKEN_KEY] = it }
    }

    override suspend fun saveData(key: Preferences.Key<String>, value: String) {
        if (shouldThrowOnSaveString) throw RuntimeException("Storage write failed")
        stringStorage[key] = value
    }

    override suspend fun saveData(key: Preferences.Key<Boolean>, value: Boolean) {
        booleanStorage[key] = value
    }

    override suspend fun getStringData(key: Preferences.Key<String>): String? {
        if (key == AUTH_TOKEN_KEY && storedToken != null) return storedToken
        return stringStorage[key]
    }

    override suspend fun getBooleanData(key: Preferences.Key<Boolean>): Boolean? =
        booleanStorage[key]

    override suspend fun clearAllData() {
        clearAllDataCalls++
        if (shouldThrowOnClearAllData) throw RuntimeException("Storage clear failed")
        stringStorage.clear()
        booleanStorage.clear()
        storedToken = null
    }
}
