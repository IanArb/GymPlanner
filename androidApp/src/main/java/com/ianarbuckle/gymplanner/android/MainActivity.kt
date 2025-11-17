package com.ianarbuckle.gymplanner.android

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ianarbuckle.gymplanner.android.availability.presentation.AvailabilityScreen
import com.ianarbuckle.gymplanner.android.availability.presentation.state.AvailabilityScreenState
import com.ianarbuckle.gymplanner.android.availability.presentation.state.PersonalTrainer
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingDetailsData
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingDetailsScreen
import com.ianarbuckle.gymplanner.android.chat.presentation.ChatScreen
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardContent
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardScreen
import com.ianarbuckle.gymplanner.android.gymlocations.presentation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.login.presentation.LoginScreen
import com.ianarbuckle.gymplanner.android.navigation.AvailabilityScreen
import com.ianarbuckle.gymplanner.android.navigation.BookingScreen
import com.ianarbuckle.gymplanner.android.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.navigation.ConversationScreen
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.IconSource
import com.ianarbuckle.gymplanner.android.navigation.LoginScreen
import com.ianarbuckle.gymplanner.android.navigation.NavigationEvent
import com.ianarbuckle.gymplanner.android.navigation.NavigationViewModel
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersDetailScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.navigation.Root
import com.ianarbuckle.gymplanner.android.navigation.ui.BottomNavigationBar
import com.ianarbuckle.gymplanner.android.navigation.ui.TopNavigationBar
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersDetailScreen
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.profile.ProfileViewModel
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportMachineBrokenScreen
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.utils.PreviewsCombined
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first
import kotlinx.datetime.LocalTime

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val navigationViewModel = hiltViewModel<NavigationViewModel>()
            val profileViewModel = hiltViewModel<ProfileViewModel>()

            val currentScreen: NavKey? = navigationViewModel.navigationBackStack.lastOrNull()

            val user by
                profileViewModel.user.collectAsStateWithLifecycle(initialValue = Pair("", ""))

            val permissionLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = {},
                )

            LaunchedEffect(Unit) {
                val rememberMe = navigationViewModel.rememberMe.first()
                if (rememberMe) {
                    navigationViewModel.onNavigate(event = NavigationEvent.NavigateToDashboard)
                } else {
                    navigationViewModel.onNavigate(event = NavigationEvent.NavigateToLogin)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val isPermissionGranted =
                            ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS) ==
                                PackageManager.PERMISSION_GRANTED

                        if (!isPermissionGranted) {
                            permissionLauncher.launch(POST_NOTIFICATIONS)
                        }
                    }
                }
            }

            GymAppTheme {
                Scaffold(
                    topBar = { TopNavigationBar(currentScreen = currentScreen) },
                    bottomBar = { BottomNavigationBar(currentScreen = currentScreen) },
                    floatingActionButton = {
                        FloatingActionButton(currentScreen = currentScreen, user = user)
                    },
                ) { contentPadding ->
                    NavigationRoot(contentPadding = contentPadding)
                }
            }
        }
    }
}

@Composable
private fun TopNavigationBar(
    currentScreen: Any?,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
) {
    if (currentScreen !is PersonalTrainersDetailScreen && currentScreen !is LoginScreen) {
        TopNavigationBar(
            currentDestination = currentScreen,
            modifier = Modifier,
            onBackClick = { navigationViewModel.onNavigate(NavigationEvent.NavigateBack) },
        )
    }
}

@Suppress("ComplexCondition")
@Composable
private fun BottomNavigationBar(
    currentScreen: Any?,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
) {
    val bottomNavItems = remember {
        persistentListOf(DashboardScreen, ReportMachineBroken, GymLocationsScreen)
    }

    if (
        currentScreen !is PersonalTrainersScreen &&
            currentScreen !is PersonalTrainersDetailScreen &&
            currentScreen !is LoginScreen &&
            currentScreen !is AvailabilityScreen &&
            currentScreen !is BookingScreen &&
            currentScreen !is ConversationScreen
    ) {
        BottomNavigationBar(
            destinations = bottomNavItems,
            currentDestination = currentScreen,
            onNavigate = { destination ->
                navigationViewModel.onNavigate(
                    event = NavigationEvent.NavigationBottomBar(destination = destination as NavKey)
                )
            },
        )
    }
}

@Composable
private fun FloatingActionButton(
    currentScreen: Any?,
    user: Pair<String, String>,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
) {
    if (currentScreen !is ConversationScreen && currentScreen !is LoginScreen) {
        FloatingActionButton(
            onClick = {
                if (user.first.isNotEmpty() && user.second.isNotEmpty()) {
                    navigationViewModel.onNavigate(
                        event =
                            NavigationEvent.NavigateToChat(
                                username = user.first,
                                userId = user.second,
                            )
                    )
                }
            },
            modifier = Modifier.testTag("Chat"),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_chat_bubble),
                contentDescription = "Open Chat",
            )
        }
    }
}

@Composable
private fun NavigationRoot(
    contentPadding: PaddingValues,
    navigationViewModel: NavigationViewModel = hiltViewModel(),
) {
    NavDisplay(
        backStack = navigationViewModel.navigationBackStack,
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider = { key ->
            when (key) {
                is Root ->
                    NavEntry(key) {
                        // No-op
                    }
                is LoginScreen ->
                    NavEntry(key) {
                        LoginScreen(
                            contentPadding = contentPadding,
                            onNavigateTo = {
                                navigationViewModel.onNavigate(NavigationEvent.NavigateToDashboard)
                            },
                        )
                    }
                is DashboardScreen ->
                    NavEntry(key) { DashboardScreen(contentPadding = contentPadding) }
                is ReportMachineBroken ->
                    NavEntry(key) { ReportMachineBrokenScreen(contentPadding = contentPadding) }
                is GymLocationsScreen ->
                    NavEntry(key) {
                        GymLocationsScreen(
                            contentPadding = contentPadding,
                            onNavigateTo = { gymLocationEnum ->
                                navigationViewModel.onNavigate(
                                    event =
                                        NavigationEvent.NavigateToPersonalTrainers(
                                            gymLocation = gymLocationEnum
                                        )
                                )
                            },
                        )
                    }
                is PersonalTrainersScreen ->
                    NavEntry(key) {
                        PersonalTrainersScreen(
                            contentPadding = contentPadding,
                            gymLocation = key.gymLocation,
                            onNavigateTo = { trainer ->
                                navigationViewModel.onNavigate(
                                    event =
                                        NavigationEvent.NavigateToPersonalTrainersDetails(
                                            name = trainer.first,
                                            bio = trainer.second,
                                            imageUrl = trainer.third,
                                        )
                                )
                            },
                            onBookClick = { personalTrainer ->
                                navigationViewModel.onNavigate(
                                    event =
                                        NavigationEvent.NavigateToAvailability(
                                            personalTrainerId = personalTrainer.id ?: "",
                                            name =
                                                personalTrainer.firstName +
                                                    " " +
                                                    personalTrainer.lastName,
                                            imageUrl = personalTrainer.imageUrl,
                                            qualifications = personalTrainer.qualifications,
                                            gymLocation = key.gymLocation.name,
                                        )
                                )
                            },
                        )
                    }
                is PersonalTrainersDetailScreen ->
                    NavEntry(key) {
                        PersonalTrainersDetailScreen(
                            contentPadding = contentPadding,
                            name = key.name,
                            bio = key.bio,
                            imageUrl = key.imageUrl,
                            onNavigateTo = {
                                navigationViewModel.onNavigate(NavigationEvent.NavigateBack)
                            },
                            onBookClick = {},
                        )
                    }
                is AvailabilityScreen ->
                    NavEntry(key) {
                        val availabilityScreenState =
                            AvailabilityScreenState(
                                personalTrainer =
                                    PersonalTrainer(
                                        personalTrainerId = key.personalTrainerId,
                                        name = key.name,
                                        imageUrl = key.imageUrl,
                                        gymLocation = key.gymLocation,
                                        qualifications = key.qualifications,
                                    )
                            )
                        AvailabilityScreen(
                            paddingValues = contentPadding,
                            availabilityScreenState = availabilityScreenState,
                            onBookingClick = { availabilityData ->
                                navigationViewModel.onNavigate(
                                    event =
                                        NavigationEvent.NavigateToBooking(
                                            personalTrainerId = key.personalTrainerId,
                                            timeSlotId = availabilityData.timeSlotId,
                                            selectedDate = availabilityData.selectedDate,
                                            selectedTimeSlot =
                                                availabilityData.selectedTimeSlot.toString(),
                                            personalTrainerName = key.name,
                                            personalTrainerAvatarUrl = key.imageUrl,
                                            location = key.gymLocation,
                                        )
                                )
                            },
                        )
                    }
                is BookingScreen ->
                    NavEntry(key) {
                        val bookingScreenState =
                            BookingDetailsData(
                                personalTrainerId = key.personalTrainerId,
                                timeSlotId = key.timeSlotId,
                                selectedDate = key.selectedDate,
                                selectedTimeSlot = LocalTime.parse(key.selectedTimeSlot),
                                personalTrainerName = key.personalTrainerName,
                                personalTrainerAvatarUrl = key.personalTrainerAvatarUrl,
                                location = key.location,
                            )

                        BookingDetailsScreen(
                            contentPadding = contentPadding,
                            bookingDetailsData = bookingScreenState,
                            navigateToHomeScreen = {
                                navigationViewModel.onNavigate(NavigationEvent.NavigateToDashboard)
                            },
                        )
                    }
                is ConversationScreen ->
                    NavEntry(key) {
                        ChatScreen(paddingValues = contentPadding, username = key.username)
                    }
                else -> NavEntry(key) { IllegalArgumentException("Unknown key: $key") }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewsCombined
@Composable
private fun DefaultPreview() {
    val navigationItems =
        persistentListOf(
            BottomNavigationItem(
                title = "Dashboard",
                selectedIcon = IconSource.FromResource(R.drawable.ic_home_icon_filled),
                unselectedIcon = IconSource.FromResource(R.drawable.ic_home_icon_outlined),
            ),
            BottomNavigationItem(
                title = "Report Machine",
                selectedIcon = IconSource.FromResource(R.drawable.ic_gym_icon_filled),
                unselectedIcon = IconSource.FromResource(R.drawable.ic_gym_icon_outlined),
            ),
            BottomNavigationItem(
                title = "Trainers",
                selectedIcon = IconSource.FromResource(R.drawable.ic_groups_icon_filled),
                unselectedIcon = IconSource.FromResource(R.drawable.ic_groups_icon_outlined),
            ),
        )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            bottomBar = {
                BottomNavigationBar(
                    destinations = navigationItems,
                    currentDestination = navigationItems[selectedItemIndex],
                    onNavigate = { selectedItemIndex = navigationItems.indexOf(it) },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chat_bubble),
                        contentDescription = "Open Chat",
                    )
                }
            },
        ) {
            DashboardContent(
                innerPadding = it,
                classes = DataProvider.fitnessClasses(),
                onViewScheduleClick = {},
                onBookPersonalTrainerClick = {},
                bookings = persistentListOf(),
            )
        }
    }
}
