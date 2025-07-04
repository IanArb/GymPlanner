package com.ianarbuckle.gymplanner.android.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Home
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable object DashboardScreen

@Serializable object ReportMachineBroken

@Serializable object GymLocationsScreen

@Serializable data class PersonalTrainersScreen(val gymLocation: GymLocation)

@Serializable
data class PersonalTrainersDetailScreen(val name: String, val bio: String, val imageUrl: String)

@Serializable object LoginScreen

@Serializable
data class AvailabilityScreen(
  val personalTrainerId: String,
  val name: String,
  val imageUrl: String,
  val gymLocation: String,
  val qualifications: List<String>,
)

@Serializable
data class BookingScreen(
  val personalTrainerId: String,
  val timeSlotId: String,
  val selectedDate: String,
  val selectedTimeSlot: String,
  val personalTrainerName: String,
  val personalTrainerAvatarUrl: String,
  val location: String,
)

fun createBottomNavigationItems(): PersistentList<BottomNavigationItem> {
  return persistentListOf(
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
    ),
  )
}

const val AvailabilityScreenPath =
  "/{personalTrainerId}" +
    "/{name}" +
    "/{imageUrl}" +
    "/{gymLocation}?qualifications={qualifications}"
const val PersonalTrainersDetailScreenPath = "/{name}/{bio}/{imageUrl}"
const val GymLocationsPath = "/{gymLocation}"
const val BookingScreenPath =
  "/{personalTrainerId}" +
    "/{timeSlotId}" +
    "/{selectedDate}" +
    "/{selectedTimeSlot}" +
    "/{personalTrainerName}" +
    "/{personalTrainerAvatarUrl}" +
    "/{location}"
