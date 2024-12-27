package gymplanner.dashboard

import androidx.datastore.preferences.core.stringPreferencesKey
import app.cash.turbine.test
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.fitnessclass.FitnessClassRepository
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.profile.ProfileRepository
import com.ianarbuckle.gymplanner.profile.domain.Profile
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import gymplanner.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTests {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val dispatcherProvider = CoroutinesDispatcherProvider(
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher,
        testCoroutineRule.testDispatcher
    )

    private val profileRepository = mockk<ProfileRepository>()
    private val fitnessClassRepository = mockk<FitnessClassRepository>()
    private val dataStoreRepository = mockk<DataStoreRepository>()

    private val clock: Clock = mockk(relaxed = true) {
        every { now() } returns Instant.parse("2023-01-01T00:00:00Z")
    }

    private val viewModel: DashboardViewModel = DashboardViewModel(
        profileRepository = profileRepository,
        fitnessClassRepository = fitnessClassRepository,
        dataStoreRepository = dataStoreRepository,
        clock = clock,
        coroutineDispatcherProvider = dispatcherProvider)

    @Test
    fun `fetchFitnessClasses should update uiState to Success when API calls succeed`() = runTest {
        // Arrange
        val userId = "user123"
        val profile = mockk<Profile>()
        val classes = persistentListOf(mockk<FitnessClass>())

        coEvery { dataStoreRepository.getStringData(stringPreferencesKey("user_id")) } returns userId
        coEvery { profileRepository.fetchProfile(userId) } returns Result.success(profile)
        coEvery { fitnessClassRepository.fetchFitnessClasses(any()) } returns Result.success(classes)

        // Act

        viewModel.fetchFitnessClasses()

        // Assert
        viewModel.uiState.test {
            assertEquals(DashboardUiState.Loading, awaitItem())
            val successState = awaitItem() as DashboardUiState.Success
            assertEquals(profile, successState.profile)
            assertEquals(classes, successState.items)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetchFitnessClasses should update uiState to Failure when API calls fail`() = runTest {
        // Arrange
        val userId = "user123"

        coEvery { dataStoreRepository.getStringData(stringPreferencesKey("user_id")) } returns userId
        coEvery { profileRepository.fetchProfile(userId) } returns Result.failure(Exception("Profile fetch failed"))
        coEvery { fitnessClassRepository.fetchFitnessClasses(any()) } returns Result.failure(Exception("Classes fetch failed"))

        // Act
        viewModel.fetchFitnessClasses()

        // Assert
        viewModel.uiState.test {
            assertEquals(DashboardUiState.Loading, awaitItem())
            assertEquals(DashboardUiState.Failure, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}