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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ianarbuckle.gymplanner.android.core.presentation.GymPlannerTheme
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardContent
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.core.presentation.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.core.utils.DataProvider
import com.ianarbuckle.gymplanner.android.reporting.data.ReportingViewModel
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormFaultReportUiState
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportingFormContent
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportingFormResponse
import com.ianarbuckle.gymplanner.android.workout.data.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val workoutViewModel: WorkoutViewModel by viewModels()

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private val reportingViewModel: ReportingViewModel by viewModels()


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        workoutViewModel.fetchClients()
        dashboardViewModel.fetchFitnessClasses()

        setContent {
            val navController = rememberNavController()
            val navigationItems = listOf(
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
                    title = "Settings",
                    selectedIcon = Icons.Filled.Settings,
                    unselectedIcon = Icons.Outlined.Settings,
                )
            )

            var selectedItemIndex by rememberSaveable {
                mutableStateOf(0)
            }

            GymPlannerTheme {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Gym Plan") }) },
                    bottomBar = {
                        NavigationBar {
                            navigationItems.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        when (index) {
                                            0 -> {
                                                navController.navigate(DashboardScreen)
                                            }
                                            1 -> {
                                                navController.navigate(ReportMachineBroken)
                                            }
                                            2 -> {
                                                navController.navigate(PersonalTrainersScreen)
                                            }
                                        }
                                        selectedItemIndex = index
                                    },
                                    label = {
                                        Text(item.title)
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else {
                                                item.unselectedIcon
                                            },
                                            contentDescription = item.title)
                                    }
                                )
                            }
                        }
                    }
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = DashboardScreen
                    ) {
                        composable<DashboardScreen> {
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

                        composable<PersonalTrainersScreen> {

                        }
                    }

                }
            }
        }
    }
}

internal data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
)

@Serializable
object DashboardScreen

@Serializable
object ReportMachineBroken

@Serializable
object PersonalTrainersScreen

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreview() {
    val navigationItems = listOf(
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
        mutableStateOf(0)
    }

    GymPlannerTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    // TODO
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            },
            bottomBar = {
                NavigationBar {
                    navigationItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            label = {
                                Text(item.title)
                            },
                            icon = {
                                Icon(
                                    imageVector = if (index == selectedItemIndex) {
                                        item.selectedIcon
                                    } else {
                                        item.unselectedIcon
                                    },
                                    contentDescription = item.title)
                            }
                        )
                    }
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
