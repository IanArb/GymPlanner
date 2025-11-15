package com.ianarbuckle.gymplanner.android.navigation

import com.ianarbuckle.gymplanner.android.R as Ui
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

@Serializable data class ConversationScreen(val username: String, val userId: String)

fun createBottomNavigationItems(): PersistentList<BottomNavigationItem> {
    return persistentListOf(
        BottomNavigationItem(
            title = "Dashboard",
            selectedIcon = IconSource.FromResource(Ui.drawable.ic_home_icon_filled),
            unselectedIcon = IconSource.FromResource(Ui.drawable.ic_home_icon_outlined),
        ),
        BottomNavigationItem(
            title = "Report Machine",
            selectedIcon = IconSource.FromResource(Ui.drawable.ic_gym_icon_filled),
            unselectedIcon = IconSource.FromResource(Ui.drawable.ic_gym_icon_outlined),
        ),
        BottomNavigationItem(
            title = "Personal Trainers",
            selectedIcon = IconSource.FromResource(Ui.drawable.ic_groups_icon_filled),
            unselectedIcon = IconSource.FromResource(Ui.drawable.ic_groups_icon_outlined),
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

const val ChatScreenPath = "/{username}/{userId}"
