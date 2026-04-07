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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeViewport
import com.ianarbuckle.gymplanner.web.ui.classes.ClassCategory
import com.ianarbuckle.gymplanner.web.ui.classes.FitnessClassItem
import com.ianarbuckle.gymplanner.web.ui.classes.UpcomingClassesSection
import com.ianarbuckle.gymplanner.web.ui.facilitystatus.EquipmentItem
import com.ianarbuckle.gymplanner.web.ui.facilitystatus.EquipmentStatus
import com.ianarbuckle.gymplanner.web.ui.facilitystatus.FacilityStatusSection
import com.ianarbuckle.gymplanner.web.ui.header.DashboardHeader
import com.ianarbuckle.gymplanner.web.ui.login.LoginScreen
import com.ianarbuckle.gymplanner.web.ui.sidebar.NavDestination
import com.ianarbuckle.gymplanner.web.ui.sidebar.SidebarNavigation
import com.ianarbuckle.gymplanner.web.ui.theme.GymPlannerColorScheme
import com.ianarbuckle.gymplanner.web.ui.theme.OffWhite
import com.ianarbuckle.gymplanner.web.ui.trainers.TodaysTeamSection
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerAvailability
import com.ianarbuckle.gymplanner.web.ui.trainers.TrainerItem
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        MaterialTheme(colorScheme = GymPlannerColorScheme) {
            var isLoggedIn by remember { mutableStateOf(false) }
            var selectedDestination by remember { mutableStateOf(NavDestination.DASHBOARD) }

            if (!isLoggedIn) {
                LoginScreen(
                    onSignInClick = { _, _, _ -> isLoggedIn = true },
                    onForgotPasswordClick = {},
                )
                return@MaterialTheme
            }

            Row(modifier = Modifier.fillMaxSize().background(OffWhite)) {
                SidebarNavigation(
                    selectedDestination = selectedDestination,
                    onDestinationSelected = { selectedDestination = it },
                    onSettingsClick = {},
                    onLogoutClick = {
                        isLoggedIn = false
                    },
                )

                Column(
                    modifier =
                        Modifier.weight(1f).fillMaxHeight().verticalScroll(rememberScrollState())
                ) {
                    DashboardHeader(userName = "Ben", userRole = "Gym Manager", onProfileClick = {})

                    Row(modifier = Modifier.fillMaxWidth().padding(24.dp)) {
                        FacilityStatusSection(
                            modifier = Modifier.weight(0.6f),
                            items =
                                listOf(
                                    EquipmentItem(
                                        name = "Treadmill 04",
                                        detail = "Sensor Fault",
                                        location = "Main Cardio Floor",
                                        status = EquipmentStatus.OUT_OF_ORDER,
                                    ),
                                    EquipmentItem(
                                        name = "Bench 02",
                                        detail = "Leather Tear",
                                        location = "Free Weights Area",
                                        status = EquipmentStatus.MAINTENANCE,
                                    ),
                                    EquipmentItem(
                                        name = "Cable Crossover 01",
                                        detail = "Lubrication Done",
                                        location = "Strength Zone",
                                        status = EquipmentStatus.OPERATIONAL,
                                    ),
                                ),
                            onViewAllClick = {},
                        )

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
