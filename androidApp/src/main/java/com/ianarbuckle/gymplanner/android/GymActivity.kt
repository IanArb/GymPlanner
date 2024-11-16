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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ianarbuckle.gymplanner.android.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.ui.common.BottomNavigationBar
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardContent
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardUiState
import com.ianarbuckle.gymplanner.android.dashboard.data.DashboardViewModel
import com.ianarbuckle.gymplanner.android.ui.common.RetryErrorScreen
import com.ianarbuckle.gymplanner.android.ui.common.TopNavigationBar
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.android.gymlocations.GymLocationsSelection
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsUiState
import com.ianarbuckle.gymplanner.android.gymlocations.data.GymLocationsViewModel
import com.ianarbuckle.gymplanner.android.login.data.LoginViewModel
import com.ianarbuckle.gymplanner.android.login.presentation.LoginScreen
import com.ianarbuckle.gymplanner.android.login.presentation.LoginState
import com.ianarbuckle.gymplanner.android.navigation.LoginScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersDetailScreen
import com.ianarbuckle.gymplanner.android.navigation.createBottomNavigationItems
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersState
import com.ianarbuckle.gymplanner.android.personaltrainers.data.PersonalTrainersViewModel
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersContent
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersDetail
import com.ianarbuckle.gymplanner.android.reporting.data.ReportingViewModel
import com.ianarbuckle.gymplanner.android.reporting.presentation.FormFaultReportUiState
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportingFormContent
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportingFormResponse
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.imageBitmapToUri
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.faultreporting.domain.FaultReport
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModels()

    private val reportingViewModel: ReportingViewModel by viewModels()

    private val gymLocationsViewModel: GymLocationsViewModel by viewModels()

    private val personalTrainersViewModel: PersonalTrainersViewModel by viewModels()

    private val loginViewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            GymAppTheme {
                Scaffold(
                    topBar = {
                        if (currentRoute != PersonalTrainersDetailScreen::class.qualifiedName.plus(("/{name}/{bio}/{imageUrl}"))
                            && currentRoute != LoginScreen::class.qualifiedName) {
                            TopNavigationBar(
                                currentRoute = currentRoute,
                                enableBackButton = currentRoute == PersonalTrainersScreen::class.qualifiedName.plus("/{gymLocation}")
                            ) {
                                navController.popBackStack()
                            }
                        }
                    },
                    bottomBar = {
                        var selectedItemIndex by rememberSaveable {
                            mutableIntStateOf(0)
                        }

                        if (currentRoute != PersonalTrainersScreen::class.qualifiedName.plus("/{gymLocation}")
                            && currentRoute != PersonalTrainersDetailScreen::class.qualifiedName.plus(("/{name}/{bio}/{imageUrl}"))
                            && currentRoute != LoginScreen::class.qualifiedName
                        ) {
                            BottomNavigationBar(
                                navController = navController,
                                navigationItems = createBottomNavigationItems(),
                                selectedItemIndex = selectedItemIndex,
                                onItemSelected = { index ->
                                    selectedItemIndex = index
                                }
                            )
                        }

                    }
                ) { contentPadding ->
                    loginViewModel.fetchNavigationEntryPoint()

                    LaunchedEffect(Unit) {
                        val rememberMe = loginViewModel.rememberMe.first()
                        if (rememberMe) {
                            navController.navigate(DashboardScreen)
                        }
                    }

                    NavHost(
                        navController = navController,
                        startDestination = LoginScreen
                    ) {
                        loginScreenGraph(contentPadding, navController)

                        dashboardBoardScreenGraph(contentPadding)

                        reportMachineScreenGraph(contentPadding)

                        gymLocationsScreenGraph(contentPadding, navController)

                        personalTrainersScreenGraph(contentPadding, navController)

                        personalTrainerDetailScreen(contentPadding, navController)
                    }
                }
            }
        }
    }

    private fun NavGraphBuilder.loginScreenGraph(contentPadding: PaddingValues, navController: NavHostController) {
        composable<LoginScreen> {
            var username by rememberSaveable { mutableStateOf("") }
            var password by rememberSaveable { mutableStateOf("") }
            var isUsernameValid by rememberSaveable { mutableStateOf(true) }
            var isPasswordValid by rememberSaveable { mutableStateOf(true) }
            var rememberMe by rememberSaveable { mutableStateOf(true) }

            val state = loginViewModel.loginState.collectAsState()

            when (state.value) {
                is LoginState.Success -> {
                    loginViewModel.persistRememberMe(rememberMe)
                    navController.navigate(DashboardScreen)
                }

                is LoginState.Error -> {
                    // Show error message
                }

                LoginState.Idle -> {
                    LoginScreen(
                        innerPaddingValues = contentPadding,
                        username = username,
                        onUsernameChange = { username = it },
                        password = password,
                        onPasswordChange = { password = it },
                        onLoginClick = {
                            if (username.isNotEmpty() && password.isNotEmpty()) {
                                loginViewModel.login(Login(username, password))
                            } else {
                                isUsernameValid = username.isNotEmpty()
                                isPasswordValid = password.isNotEmpty()
                            }
                        },
                        isUsernameValid = isUsernameValid,
                        isPasswordValid = isPasswordValid,
                        onUsernameInvalid = { isUsernameValid = it },
                        onPasswordInvalid = { isPasswordValid = it },
                        rememberMe = rememberMe,
                        onRememberMeChange = { rememberMe = it },
                        isLoading = false
                    )
                }
                LoginState.Loading -> {
                    LoginScreen(
                        innerPaddingValues = contentPadding,
                        username = username,
                        onUsernameChange = { username = it },
                        password = password,
                        onPasswordChange = { password = it },
                        rememberMe = rememberMe,
                        onRememberMeChange = { rememberMe = it },
                        isLoading = true
                    )
                }
            }


        }
    }

    private fun NavGraphBuilder.dashboardBoardScreenGraph(contentPadding: PaddingValues) {
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
    }

    private fun NavGraphBuilder.reportMachineScreenGraph(contentPadding: PaddingValues) {
        composable<ReportMachineBroken> {
            var machineNumber by rememberSaveable { mutableStateOf("") }
            var description by rememberSaveable { mutableStateOf("") }
            var imageBitmap by rememberSaveable { mutableStateOf<Bitmap?>(null) }
            var isLoading by rememberSaveable { mutableStateOf(false) }
            var hasFailed by rememberSaveable { mutableStateOf(false) }
            var isMachineNumberValid by rememberSaveable { mutableStateOf(false) }
            var isDescriptionValid by rememberSaveable { mutableStateOf(false) }
            var isImageBitMapValid by rememberSaveable { mutableStateOf(false) }
            var hasMachineNumberInteracted by rememberSaveable { mutableStateOf(false) }
            var hasDescriptionInteracted by rememberSaveable { mutableStateOf(false) }
            var hasPhotoInteracted by rememberSaveable { mutableStateOf(false) }

            val launchCamera = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicturePreview()
            ) { bitmap ->
                imageBitmap = bitmap
            }

            val context = LocalContext.current

            when (val value = reportingViewModel.uiState.collectAsState().value) {
                is FormFaultReportUiState.FormLoading -> {
                    ReportingFormContent(
                        innerPadding = contentPadding,
                        machineNumber = machineNumber,
                        onMachineNumberChange = {
                            machineNumber = it
                            isMachineNumberValid = it.isNotEmpty()
                            hasMachineNumberInteracted = true
                        },
                        description = description,
                        onDescriptionChange = {
                            description = it
                            isDescriptionValid = it.isNotEmpty()
                            hasDescriptionInteracted = true
                        },
                        imageBitmap = imageBitmap?.asImageBitmap(),
                        isLoading = true,
                        hasFailed = hasFailed,
                        isMachineNumberValid = isMachineNumberValid,
                        isDescriptionValid = isDescriptionValid,
                        isImageBitMapValid = isImageBitMapValid,
                        hasMachineNumberInteracted = hasMachineNumberInteracted,
                        hasDescriptionInteracted = hasDescriptionInteracted,
                        hasPhotoInteracted = hasPhotoInteracted,
                        onPhotoClick = {
                            hasPhotoInteracted = true
                            launchCamera.launch()
                        },
                        onSendClick = {

                        }
                    )
                }

                is FormFaultReportUiState.FormSuccess -> {
                    ReportingFormResponse(
                        innerPadding = contentPadding,
                        faultReport = value.data
                    ) {
                        machineNumber = ""
                        description = ""
                        imageBitmap = null
                        isLoading = false
                        hasFailed = false
                        isMachineNumberValid = false
                        isDescriptionValid = false
                        isImageBitMapValid = false
                        hasMachineNumberInteracted = false
                        hasDescriptionInteracted = false
                        hasPhotoInteracted = false
                    }
                }

                is FormFaultReportUiState.FormError -> {
                    ReportingFormContent(
                        innerPadding = contentPadding,
                        machineNumber = machineNumber,
                        onMachineNumberChange = {
                            machineNumber = it
                            isMachineNumberValid = it.isNotEmpty()
                            hasMachineNumberInteracted = true
                        },
                        description = description,
                        onDescriptionChange = {
                            description = it
                            isDescriptionValid = it.isNotEmpty()
                            hasDescriptionInteracted = true
                        },
                        imageBitmap = imageBitmap?.asImageBitmap(),
                        isLoading = false,
                        hasFailed = true,
                        isMachineNumberValid = isMachineNumberValid,
                        isDescriptionValid = isDescriptionValid,
                        isImageBitMapValid = isImageBitMapValid,
                        hasMachineNumberInteracted = hasMachineNumberInteracted,
                        hasDescriptionInteracted = hasDescriptionInteracted,
                        hasPhotoInteracted = hasPhotoInteracted,
                        onPhotoClick = {
                            hasPhotoInteracted = true
                            launchCamera.launch()
                        },
                        onSendClick = {
                            hasMachineNumberInteracted = true
                            hasDescriptionInteracted = true
                            hasPhotoInteracted = true

                            isMachineNumberValid = machineNumber.isNotEmpty()
                            isDescriptionValid = description.isNotEmpty()
                            isImageBitMapValid = imageBitmap != null

                            if (isMachineNumberValid && isDescriptionValid && isImageBitMapValid) {
                                reportingViewModel.submitFault(
                                    FaultReport(
                                        machineNumber = machineNumber.toInt(),
                                        description = description,
                                        photoUri = imageBitmap?.asImageBitmap()?.imageBitmapToUri(
                                            context,
                                            "machine_fault_report-${machineNumber}.png"
                                        ).toString(),
                                        date = System.currentTimeMillis().toString(),
                                    )
                                )
                            }
                        }
                    )
                }

                FormFaultReportUiState.FormIdle -> {
                    ReportingFormContent(
                        innerPadding = contentPadding,
                        machineNumber = machineNumber,
                        onMachineNumberChange = {
                            machineNumber = it
                            isMachineNumberValid = it.isNotEmpty()
                            hasMachineNumberInteracted = true
                        },
                        description = description,
                        onDescriptionChange = {
                            description = it
                            isDescriptionValid = it.isNotEmpty()
                            hasDescriptionInteracted = true
                        },
                        imageBitmap = imageBitmap?.asImageBitmap(),
                        isLoading = false,
                        hasFailed = hasFailed,
                        isMachineNumberValid = isMachineNumberValid,
                        isDescriptionValid = isDescriptionValid,
                        isImageBitMapValid = isImageBitMapValid,
                        hasMachineNumberInteracted = hasMachineNumberInteracted,
                        hasDescriptionInteracted = hasDescriptionInteracted,
                        hasPhotoInteracted = hasPhotoInteracted,
                        onPhotoClick = {
                            hasPhotoInteracted = true
                            launchCamera.launch()
                        },
                        onSendClick = {
                            hasMachineNumberInteracted = true
                            hasDescriptionInteracted = true
                            hasPhotoInteracted = true

                            isMachineNumberValid = machineNumber.isNotEmpty()
                            isDescriptionValid = description.isNotEmpty()
                            isImageBitMapValid = imageBitmap != null

                            if (isMachineNumberValid && isDescriptionValid && isImageBitMapValid) {
                                reportingViewModel.submitFault(
                                    FaultReport(
                                        machineNumber = machineNumber.toInt(),
                                        description = description,
                                        photoUri = imageBitmap?.asImageBitmap()?.imageBitmapToUri(
                                            context,
                                            "machine_fault_report-${machineNumber}.png"
                                        ).toString(),
                                        date = System.currentTimeMillis().toString(),
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    private fun NavGraphBuilder.gymLocationsScreenGraph(
        contentPadding: PaddingValues,
        navController: NavHostController
    ) {
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

                            "Dun Laoghaoire" -> {
                                navController.navigate(
                                    PersonalTrainersScreen(
                                        GymLocation.DUNLOAGHAIRE
                                    )
                                )
                            }

                            "Sandymount" -> {
                                navController.navigate(
                                    PersonalTrainersScreen(
                                        GymLocation.SANDYMOUNT
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

    private fun NavGraphBuilder.personalTrainersScreenGraph(
        contentPadding: PaddingValues,
        navController: NavHostController,
    ) {
        composable<PersonalTrainersScreen> {
            val args = it.toRoute<PersonalTrainersScreen>()
            personalTrainersViewModel.fetchPersonalTrainers(args.gymLocation)

            when (val uiState = personalTrainersViewModel.uiState.collectAsState().value) {
                is PersonalTrainersState.Failure -> {
                    RetryErrorScreen(
                        text = "Failed to retrieve personal trainers."
                    ) {
                        personalTrainersViewModel.fetchPersonalTrainers(args.gymLocation)
                    }
                }

                is PersonalTrainersState.Success -> {
                    PersonalTrainersContent(
                        innerPadding = contentPadding,
                        personalTrainers = uiState.personalTrainers.toImmutableList(),
                        onSocialLinkClick = {

                        },
                        onBookTrainerClick = { },
                        onItemClick = { item ->
                            navController.navigate(
                                PersonalTrainersDetailScreen(
                                    item.first,
                                    item.second,
                                    item.third
                                )
                            )
                        }
                    )
                }

                is PersonalTrainersState.Loading -> {
                    CircularProgressIndicator()
                }
            }
        }
    }

    private fun NavGraphBuilder.personalTrainerDetailScreen(contentPadding: PaddingValues, navController: NavHostController) {
        composable<PersonalTrainersDetailScreen> {
            val args = it.toRoute<PersonalTrainersDetailScreen>()

            PersonalTrainersDetail(
                contentPadding = contentPadding,
                name = args.name,
                bio = args.bio,
                imageUrl = args.imageUrl,
                onBookClick = {},
                onBackClick = {
                    navController.popBackStack()
                }
            )
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
