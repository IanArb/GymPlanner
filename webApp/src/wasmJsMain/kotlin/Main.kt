import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import co.touchlab.kermit.Logger
import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.DefaultAuthenticationRepository
import com.ianarbuckle.gymplanner.common.GymLocation
import com.ianarbuckle.gymplanner.di.initKoin
import com.ianarbuckle.gymplanner.facilities.DefaultFacilitiesRepository
import com.ianarbuckle.gymplanner.facilities.FacilitiesRepository
import com.ianarbuckle.gymplanner.web.ui.classes.ClassCategory
import com.ianarbuckle.gymplanner.web.ui.classes.FitnessClassItem
import com.ianarbuckle.gymplanner.web.ui.classes.UpcomingClassesSection
import com.ianarbuckle.gymplanner.web.ui.data.DashboardUiState
import com.ianarbuckle.gymplanner.web.ui.data.DashboardViewModel
import com.ianarbuckle.gymplanner.web.ui.facilitystatus.FacilityStatusSection
import com.ianarbuckle.gymplanner.web.ui.header.DashboardHeader
import com.ianarbuckle.gymplanner.web.ui.login.LoginAction
import com.ianarbuckle.gymplanner.web.ui.login.LoginScreen
import com.ianarbuckle.gymplanner.web.ui.login.LoginUiState
import com.ianarbuckle.gymplanner.web.ui.login.LoginViewModel
import com.ianarbuckle.gymplanner.web.ui.sidebar.NavDestination
import com.ianarbuckle.gymplanner.web.ui.sidebar.SidebarNavigation
import com.ianarbuckle.gymplanner.web.ui.theme.GymPlannerColorScheme
import com.ianarbuckle.gymplanner.web.ui.theme.OffWhite
import com.ianarbuckle.gymplanner.web.ui.trainers.TodaysTeamSection
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerAvailability
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerItem
import kotlinx.browser.document
import kotlinx.collections.immutable.toImmutableList
import org.koin.dsl.module

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Logger.setLogWriters(BrowserLogWriter())

    initKoin(
        enableNetworkLogs = true,
        baseUrl = "https://4dbc-2001-bb6-70d9-f658-ac77-e264-2d64-76b5.ngrok-free.app",
        websocketBaseUrl = "ws://gymplanner-api-production.up.railway.app",
        appDeclaration = {
            modules(
                module { single<AuthenticationRepository> { DefaultAuthenticationRepository() } },
                module { single<FacilitiesRepository> { DefaultFacilitiesRepository() } },
            )
        },
    )
    ComposeViewport(document.body!!) {
        MaterialTheme(colorScheme = GymPlannerColorScheme) {
            var selectedDestination by remember { mutableStateOf(NavDestination.DASHBOARD) }

            val scope = rememberCoroutineScope()
            val loginViewModel = remember { LoginViewModel(scope) }
            val loginUiState by loginViewModel.uiState.collectAsState()
            val isAuthenticated by loginViewModel.isAuthenticated.collectAsState()
            val isCheckingAuth by loginViewModel.isCheckingAuth.collectAsState()

            val dashboardViewModel = remember { DashboardViewModel(scope) }

            if (isCheckingAuth) return@MaterialTheme

            if (!isAuthenticated) {
                LoginScreen(
                    onSignInClick = { username, password ->
                        loginViewModel.dispatchAction(
                            LoginAction.Login(username = username, password = password)
                        )
                    },
                    onForgotPasswordClick = {},
                    isLoading = loginUiState is LoginUiState.Loading,
                    authError = (loginUiState as? LoginUiState.Error)?.message,
                )
                return@MaterialTheme
            }

            Row(modifier = Modifier.fillMaxSize().background(OffWhite)) {
                SidebarNavigation(
                    selectedDestination = selectedDestination,
                    onDestinationSelected = { selectedDestination = it },
                    onSettingsClick = {},
                    onLogoutClick = { loginViewModel.dispatchAction(LoginAction.Logout) },
                )

                Column(
                    modifier =
                        Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())
                ) {
                    DashboardHeader(userName = "Ben", userRole = "Gym Manager", onProfileClick = {})

                    LaunchedEffect(Unit) {
                        dashboardViewModel.fetchFacilities(gymLocation = GymLocation.CLONTARF)
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        when (val uiState = dashboardViewModel.uiState.collectAsState().value) {
                            is DashboardUiState.Idle -> {}
                            is DashboardUiState.Loading -> {
                                // Show loading state (e.g., a progress indicator)
                            }
                            is DashboardUiState.Error -> {}
                            is DashboardUiState.Success -> {
                                FacilityStatusSection(
                                    modifier = Modifier.weight(0.6f),
                                    items = uiState.facilities,
                                    onViewAllClick = {},
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        TodaysTeamSection(
                            modifier = Modifier.weight(0.4f),
                            trainers =
                                listOf(
                                    TrainerItem(
                                        name = "Sarah Jenkins",
                                        availability = TrainerAvailability.AVAILABLE,
                                    ),
                                    TrainerItem(
                                        name = "David Chen",
                                        availability = TrainerAvailability.IN_SESSION,
                                    ),
                                    TrainerItem(
                                        name = "Elena Rodriguez",
                                        availability = TrainerAvailability.AVAILABLE,
                                    ),
                                    TrainerItem(
                                        name = "Rosa Rodriguez",
                                        availability = TrainerAvailability.AVAILABLE,
                                    ),
                                ),
                        )
                    }

                    UpcomingClassesSection(
                        modifier =
                            Modifier.fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 8.dp)
                                .padding(bottom = 24.dp),
                        classes =
                            listOf(
                                FitnessClassItem(
                                    name = "HIIT Performance",
                                    category = ClassCategory.PEAK_HOUR,
                                    timeSlot = "10:30 AM - 11:30 AM",
                                    coachName = "Sarah Jenkins",
                                ),
                                FitnessClassItem(
                                    name = "Vinyasa Flow",
                                    category = ClassCategory.RECOVERY,
                                    timeSlot = "12:00 PM - 01:00 PM",
                                    coachName = "Elena Rodriguez",
                                ),
                                FitnessClassItem(
                                    name = "Power Lifting",
                                    category = ClassCategory.STRENGTH,
                                    timeSlot = "04:30 PM - 06:00 PM",
                                    coachName = "Marcus Thorne",
                                ),
                            ),
                    )
                }
            }
        }
    }
}
