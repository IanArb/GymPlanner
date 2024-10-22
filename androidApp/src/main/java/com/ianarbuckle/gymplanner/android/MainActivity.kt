package com.ianarbuckle.gymplanner.android

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ianarbuckle.gymplanner.android.core.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.core.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.core.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.core.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.core.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.commonUi.BottomNavigationBar
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardContent
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.commonUi.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.commonUi.TopNavigationBar
import com.ianarbuckle.gymplanner.android.core.utils.DataProvider
import com.ianarbuckle.gymplanner.android.gymlocations.GymLocationsSelection
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.reporting.data.ReportingViewModel
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormFaultReportUiState
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportingFormContent
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportingFormResponse
import com.ianarbuckle.gymplanner.android.core.theme.GymAppTheme
import com.ianarbuckle.gymplanner.model.GymLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private val reportingViewModel: ReportingViewModel by viewModels()

    private val gymLocationsViewModel: GymLocationsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val navigationItems = persistentListOf(
                BottomNavigationItem(
                    title = "Dashboard",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                ),
                BottomNavigationItem(
                    title = "Report Machine",
                    selectedIcon = Icons.Filled.Build,
                    unselectedIcon = Icons.Outlined.Build,
                ),
                BottomNavigationItem(
                    title = "Personal Trainers",
                    selectedIcon = Icons.Filled.Face,
                    unselectedIcon = Icons.Outlined.Face,
                )
            )

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            GymAppTheme {
                Scaffold(
                    topBar = {
                        if (currentRoute == PersonalTrainersScreen::class.qualifiedName.plus("/{gymLocation}")) {
                            TopNavigationBar(
                                currentRoute = currentRoute,
                                enableBackButton = true) {
                                navController.popBackStack()
                            }
                        } else {
                            TopNavigationBar(currentRoute)
                        }
                    },
                    bottomBar = {
                        var selectedItemIndex by rememberSaveable {
                            mutableIntStateOf(0)
                        }

                        if (currentRoute != PersonalTrainersScreen::class.qualifiedName.plus("/{gymLocation}")) {
                            BottomNavigationBar(
                                navController = navController,
                                navigationItems = navigationItems,
                                selectedItemIndex = selectedItemIndex,
                                onItemSelected = { index ->
                                    selectedItemIndex = index
                                }
                            )
                        }

                    }
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = DashboardScreen
                    ) {
                        composable<DashboardScreen> {
                            dashboardViewModel.fetchFitnessClasses()

                            val uiState = dashboardViewModel.uiState.collectAsState()

                            when (uiState.value) {
                                is DashboardUiState.Failure -> {
                                    RetryErrorScreen(
                                        text = "Failed to retrieve dashboard."
                                    ) {
                                        dashboardViewModel.fetchFitnessClasses()
                                    }
                                }
                                is DashboardUiState.FitnessClasses -> {
                                    DashboardContent(
                                        innerPadding = contentPadding,
                                        classesCarouselItems = (uiState.value as DashboardUiState.FitnessClasses).items,
                                        onViewScheduleClick = {

                                        }
                                    )
                                }
                                is DashboardUiState.Loading -> {
                                    CircularProgressIndicator()
                                }
                            }

                        }

                        composable<ReportMachineBroken> {
                            var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
                            var resetForm by remember { mutableStateOf(false) }

                            val launchCamera = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.TakePicturePreview()
                            ) { bitmap ->
                                imageBitmap = bitmap
                            }

                            when (val value = reportingViewModel.uiState.collectAsState().value) {
                                is FormFaultReportUiState.FormLoading -> {
                                    ReportingFormContent(
                                        innerPadding = contentPadding,
                                        imageBitmap = imageBitmap?.asImageBitmap(),
                                        isLoading = true,
                                        onPhotoClick = {
                                            launchCamera.launch()
                                        },
                                        onSendClick = { },
                                    )
                                }
                                is FormFaultReportUiState.FormSuccess -> {
                                    if (resetForm) {
                                        ReportingFormContent(
                                            innerPadding = contentPadding,
                                            imageBitmap = imageBitmap?.asImageBitmap(),
                                            onPhotoClick = {
                                                launchCamera.launch()
                                            },
                                            isLoading = false,
                                            onSendClick = { data ->
                                                resetForm = false
                                                reportingViewModel.submitFault(data)
                                            },
                                        )
                                    } else {
                                        ReportingFormResponse(innerPadding = contentPadding, faultReport = value.data) {
                                            imageBitmap = null
                                            resetForm = true
                                        }
                                    }
                                }
                                is FormFaultReportUiState.FormError -> {
                                    ReportingFormContent(
                                        innerPadding = contentPadding,
                                        imageBitmap = imageBitmap?.asImageBitmap(),
                                        isLoading = false,
                                        hasFailed = true,
                                        onPhotoClick = {
                                            launchCamera.launch()
                                        },
                                        onSendClick = { data ->
                                            reportingViewModel.submitFault(data)
                                        },
                                    )
                                }

                                FormFaultReportUiState.FormIdle -> {
                                    ReportingFormContent(
                                        innerPadding = contentPadding,
                                        imageBitmap = imageBitmap?.asImageBitmap(),
                                        onPhotoClick = {
                                            launchCamera.launch()
                                        },
                                        onSendClick = { data ->
                                            reportingViewModel.submitFault(data)
                                        },
                                    )
                                }
                            }

                        }

                        composable<GymLocationsScreen> {
                            gymLocationsViewModel.fetchGymLocations()

                            when (val uiState = gymLocationsViewModel.uiState.collectAsState().value) {
                                is GymLocationsUiState.Failure -> {
                                    RetryErrorScreen(
                                        text = "Failed to retrieve gym locations."
                                    ) {
                                        gymLocationsViewModel.fetchGymLocations()
                                    }
                                }
                                is GymLocationsUiState.Success -> {
                                    GymLocationsSelection(
                                        innerPadding = contentPadding,
                                        gyms = uiState.gymLocations.toImmutableList(),
                                    ) {
                                        when (it.title) {
                                            "Clontarf" -> {
                                                navController.navigate(
                                                    PersonalTrainersScreen(
                                                        GymLocation.CLONTARF
                                                    )
                                                )
                                            }
                                            "Aston Quay" -> {
                                                navController.navigate(
                                                    PersonalTrainersScreen(
                                                        GymLocation.ASTONQUAY
                                                    )
                                                )
                                            }
                                            "Leopardstown" -> {
                                                navController.navigate(
                                                    PersonalTrainersScreen(
                                                        GymLocation.LEOPARDSTOWN
                                                    )
                                                )
                                            }
                                            "Westmanstown" -> {
                                                navController.navigate(
                                                    PersonalTrainersScreen(
                                                        GymLocation.WESTMANSTOWN
                                                    )
                                                )
                                            }
                                            "Dun Laoghaire" -> {
                                                navController.navigate(
                                                    PersonalTrainersScreen(
                                                        GymLocation.DUNLOAGHAIRE
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                                is GymLocationsUiState.Loading -> {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreview() {
    val navigationItems = persistentListOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            title = "Chat",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email,
        ),
        BottomNavigationItem(
            title = "Trainers",
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face,
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val navController = rememberNavController()

    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    navigationItems = navigationItems,
                    selectedItemIndex = selectedItemIndex) {
                    selectedItemIndex = it
                }
            }
        ) {
            DashboardContent(
                innerPadding = it,
                classesCarouselItems = DataProvider.fitnessClasses(),
                onViewScheduleClick = { }
            )
        }
    }
}
