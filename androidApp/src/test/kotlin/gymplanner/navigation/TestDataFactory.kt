package gymplanner.navigation

import com.ianarbuckle.gymplanner.android.navigation.AvailabilityScreen
import com.ianarbuckle.gymplanner.android.navigation.BookingScreen
import com.ianarbuckle.gymplanner.android.navigation.ConversationScreen
import com.ianarbuckle.gymplanner.android.navigation.NavigationEvent
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersDetailScreen
import com.ianarbuckle.gymplanner.android.navigation.PersonalTrainersScreen
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation

object TestDataFactory {

    fun createNavigateToAvailabilityEvent(
        personalTrainerId: String = "1",
        name: String = "John Doe",
        imageUrl: String = "url",
        gymLocation: String = GymLocation.CLONTARF.name,
        qualifications: List<String> = listOf("Cert 3", "Cert 4"),
    ) =
        NavigationEvent.NavigateToAvailability(
            personalTrainerId = personalTrainerId,
            name = name,
            imageUrl = imageUrl,
            gymLocation = gymLocation,
            qualifications = qualifications,
        )

    fun createAvailabilityScreen(
        personalTrainerId: String = "1",
        name: String = "John Doe",
        imageUrl: String = "url",
        gymLocation: String = GymLocation.CLONTARF.name,
        qualifications: List<String> = listOf("Cert 3", "Cert 4"),
    ) =
        AvailabilityScreen(
            personalTrainerId = personalTrainerId,
            name = name,
            imageUrl = imageUrl,
            gymLocation = gymLocation,
            qualifications = qualifications,
        )

    fun createNavigateToBookingEvent(
        personalTrainerId: String = "1",
        timeSlotId: String = "slot1",
        selectedDate: String = "2024-01-01",
        selectedTimeSlot: String = "10:00",
        personalTrainerName: String = "John Doe",
        personalTrainerAvatarUrl: String = "url",
        location: String = "Clontarf",
    ) =
        NavigationEvent.NavigateToBooking(
            personalTrainerId = personalTrainerId,
            timeSlotId = timeSlotId,
            selectedDate = selectedDate,
            selectedTimeSlot = selectedTimeSlot,
            personalTrainerName = personalTrainerName,
            personalTrainerAvatarUrl = personalTrainerAvatarUrl,
            location = location,
        )

    fun createBookingScreen(
        personalTrainerId: String = "1",
        timeSlotId: String = "slot1",
        selectedDate: String = "2024-01-01",
        selectedTimeSlot: String = "10:00",
        personalTrainerName: String = "John Doe",
        personalTrainerAvatarUrl: String = "url",
        location: String = "Clontarf",
    ) =
        BookingScreen(
            personalTrainerId = personalTrainerId,
            timeSlotId = timeSlotId,
            selectedDate = selectedDate,
            selectedTimeSlot = selectedTimeSlot,
            personalTrainerName = personalTrainerName,
            personalTrainerAvatarUrl = personalTrainerAvatarUrl,
            location = location,
        )

    fun createNavigateToChatEvent(username: String = "JohnDoe", userId: String = "123") =
        NavigationEvent.NavigateToChat(username, userId)

    fun createConversationScreen(username: String = "JohnDoe", userId: String = "123") =
        ConversationScreen(username, userId)

    fun createNavigateToPersonalTrainersEvent(gymLocation: GymLocation = GymLocation.CLONTARF) =
        NavigationEvent.NavigateToPersonalTrainers(gymLocation)

    fun createPersonalTrainersScreen(gymLocation: GymLocation = GymLocation.CLONTARF) =
        PersonalTrainersScreen(gymLocation)

    fun createNavigateToPersonalTrainersDetailsEvent(
        name: String = "John Doe",
        bio: String = "Bio",
        imageUrl: String = "imageUrl",
    ) = NavigationEvent.NavigateToPersonalTrainersDetails(name, bio, imageUrl)

    fun createPersonalTrainersDetailScreen(
        name: String = "John Doe",
        bio: String = "Bio",
        imageUrl: String = "imageUrl",
    ) = PersonalTrainersDetailScreen(name, bio, imageUrl)
}
