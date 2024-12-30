package com.ianarbuckle.gymplanner.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ianarbuckle.gymplanner.android.booking.presentation.BookingScreen
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardContent
import com.ianarbuckle.gymplanner.android.dashboard.presentation.DashboardScreen
import com.ianarbuckle.gymplanner.android.gymlocations.presentation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.login.presentation.LoginScreen
import com.ianarbuckle.gymplanner.android.navigation.BookTrainerScreen
import com.ianarbuckle.gymplanner.android.navigation.BottomNavigationItem
import com.ianarbuckle.gymplanner.android.navigation.DashboardScreen
import com.ianarbuckle.gymplanner.android.navigation.GymLocationsScreen
import com.ianarbuckle.gymplanner.android.navigation.LoginScreen
import com.ianarbuckle.gymplanner.android.navigation.NavigationViewModel
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersDetailScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.navigation.ReportMachineBroken
import com.ianarbuckle.gymplanner.android.navigation.createBottomNavigationItems
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersDetailScreen
import com.ianarbuckle.gymplanner.android.personaltrainers.presentation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.android.reporting.presentation.ReportMachineBrokenScreen
import com.ianarbuckle.gymplanner.android.ui.common.BottomNavigationBar
import com.ianarbuckle.gymplanner.android.ui.common.TopNavigationBar
import com.ianarbuckle.gymplanner.android.ui.theme.GymAppTheme
import com.ianarbuckle.gymplanner.android.utils.DataProvider
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Suppress("CyclomaticComplexMethod", "LongMethod")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navigationViewModel = hiltViewModel<NavigationViewModel>()
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            LaunchedEffect(true) {
                val rememberMe = navigationViewModel.rememberMe.first()
                if (rememberMe) {
                    navController.navigate(DashboardScreen)
                }
            }

            GymAppTheme {
                Scaffold(
                    topBar = {
                        if (currentRoute != PersonalTrainersDetailScreen::class.qualifiedName
                                .plus(("/{name}/{bio}/{imageUrl}")) &&
                            currentRoute != LoginScreen::class.qualifiedName
                        ) {
                            TopNavigationBar(
                                currentRoute = currentRoute,
                                enableBackButton =
                                currentRoute == PersonalTrainersScreen::class.qualifiedName
                                    .plus("/{gymLocation}") ||
                                    currentRoute == BookTrainerScreen::class.qualifiedName
                                    .plus(
                                        "/{personalTrainerId}/{name}/{imageUrl}?qualifications={qualifications}",
                                    ),
                                modifier = Modifier,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                            )
                        }
                    },
                    bottomBar = {
                        var selectedItemIndex by remember {
                            mutableIntStateOf(0)
                        }

                        if (currentRoute != PersonalTrainersScreen::class.qualifiedName.plus("/{gymLocation}") &&
                            currentRoute != PersonalTrainersDetailScreen::class.qualifiedName.plus(
                                ("/{name}/{bio}/{imageUrl}"),
                            ) &&
                            currentRoute != LoginScreen::class.qualifiedName &&
                            currentRoute != BookTrainerScreen::class.qualifiedName.plus(
                                ("/{personalTrainerId}/{name}/{imageUrl}?qualifications={qualifications}"),
                            )
                        ) {
                            BottomNavigationBar(
                                navigationItems = createBottomNavigationItems(),
                                selectItemIndex = selectedItemIndex,
                                onItemSelect = { index ->
                                    selectedItemIndex = index
                                },
                                onNavigateTo = { index ->
                                    when (index) {
                                        0 -> navController.navigate(DashboardScreen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                        }
                                        1 -> navController.navigate(ReportMachineBroken) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                        }
                                        2 -> navController.navigate(GymLocationsScreen) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                        }
                                    }
                                },
                            )
                        }
                    },
                ) { contentPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = LoginScreen,
                    ) {
                        composable<LoginScreen> {
                            LoginScreen(
                                contentPadding = contentPadding,
                                onNavigateTo = {
                                    navController.navigate(DashboardScreen)
                                }
                            )
                        }

                        composable<DashboardScreen> {
                            DashboardScreen(
                                contentPadding = contentPadding,
                            )
                        }

                        composable<ReportMachineBroken> {
                            ReportMachineBrokenScreen(
                                contentPadding = contentPadding,
                            )
                        }

                        composable<GymLocationsScreen> {
                            GymLocationsScreen(
                                contentPadding = contentPadding,
                                onNavigateTo = { gymLocation ->
                                    when (gymLocation) {
                                        "Clontarf" -> {
                                            navController.navigate(
                                                PersonalTrainersScreen(
                                                    gymLocation = GymLocation.CLONTARF
                                                )
                                            ) {
                                                restoreState = true
                                            }
                                        }

                                        "Aston Quay" -> {
                                            navController.navigate(
                                                PersonalTrainersScreen(
                                                    gymLocation = GymLocation.ASTONQUAY
                                                )
                                            ) {
                                                restoreState = true
                                            }
                                        }

                                        "Leopardstown" -> {
                                            navController.navigate(
                                                PersonalTrainersScreen(
                                                    gymLocation = GymLocation.LEOPARDSTOWN
                                                )
                                            ) {
                                                restoreState = true
                                            }
                                        }

                                        "Westmanstown" -> {
                                            navController.navigate(
                                                PersonalTrainersScreen(
                                                    gymLocation = GymLocation.WESTMANSTOWN
                                                )
                                            ) {
                                                restoreState = true
                                            }
                                        }

                                        "Dun Laoghaoire" -> {
                                            navController.navigate(
                                                PersonalTrainersScreen(
                                                    GymLocation.DUNLOAGHAIRE
                                                )
                                            ) {
                                                restoreState = true
                                            }
                                        }

                                        "Sandymount" -> {
                                            navController.navigate(
                                                PersonalTrainersScreen(
                                                    GymLocation.SANDYMOUNT
                                                )
                                            ) {
                                                restoreState = true
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        composable<PersonalTrainersScreen> {
                            val args = it.toRoute<PersonalTrainersScreen>()
                            PersonalTrainersScreen(
                                contentPadding = contentPadding,
                                gymLocation = args.gymLocation,
                                onNavigateTo = { trainer ->
                                    navController.navigate(
                                        PersonalTrainersDetailScreen(
                                            name = trainer.first,
                                            bio = trainer.second,
                                            imageUrl = trainer.third,
                                        ),
                                    )
                                },
                                onBookClick = { personalTrainer ->
                                    navController.navigate(
                                        BookTrainerScreen(
                                            personalTrainerId = personalTrainer.id ?: "",
                                            name = personalTrainer.firstName + " " + personalTrainer.lastName,
                                            imageUrl = personalTrainer.imageUrl,
                                            qualifications = personalTrainer.qualifications,
                                        ),
                                    )
                                },
                            )
                        }

                        composable<PersonalTrainersDetailScreen> {
                            val args = it.toRoute<PersonalTrainersDetailScreen>()
                            PersonalTrainersDetailScreen(
                                contentPadding = contentPadding,
                                name = args.name,
                                bio = args.bio,
                                imageUrl = args.imageUrl,
                                onNavigateTo = {
                                    navController.popBackStack()
                                },
                                onBookClick = {
                                },
                            )
                        }

                        composable<BookTrainerScreen> {
                            val args = it.toRoute<BookTrainerScreen>()
                            val name = args.name
                            val imageUrl = args.imageUrl
                            val qualifications = args.qualifications
                            BookingScreen(
                                paddingValues = contentPadding,
                                name = name,
                                imageUrl = imageUrl,
                                qualifications = qualifications,
                            )
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
private fun DefaultPreview() {
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
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    GymAppTheme {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Gym Plan") }) },
            bottomBar = {
                BottomNavigationBar(
                    navigationItems = navigationItems,
                    selectItemIndex = selectedItemIndex,
                    onItemSelect = { index ->
                        selectedItemIndex = index
                    },
                    onNavigateTo = { },
                )
            },
        ) {
            DashboardContent(
                innerPadding = it,
                items = DataProvider.fitnessClasses(),
                onViewScheduleClick = { },
                onBookPersonalTrainerClick = {
                },
            )
        }
    }
}
