package gymplanner.utils

import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.fitnessclass.domain.Duration
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

object DataProvider {

    fun carouselItems(
        classes: ImmutableList<FitnessClass> = persistentListOf(
            createFitnessClass(
                dayOfWeek = "MONDAY",
                name = "Pilates Class",
                description = "Come join our pilates class today!",
                imageUrl = IMAGE_URL,
                startTime = "07:00:00",
                endTime = "08:00:00",
                duration = Duration(
                    value = 3600,
                    unit = "SECONDS"
                )
            ),
            createFitnessClass(
                dayOfWeek = "MONDAY",
                name = "Strength training",
                description = "We will be focusing on lower body today!",
                imageUrl = IMAGE_URL,
                startTime = "07:00:00",
                endTime = "08:00:00",
                duration = Duration(
                    value = 3600,
                    unit = "SECONDS"
                )
            ),
            createFitnessClass(
                dayOfWeek = "MONDAY",
                name = "Body pump",
                description = "Come join us for body bump!",
                imageUrl = IMAGE_URL,
                startTime = "07:00:00",
                endTime = "08:00:00",
                duration = Duration(
                    value = 3600,
                    unit = "SECONDS"
                )
            )
        )
    ) = classes

    private fun createFitnessClass(
        dayOfWeek: String = "MONDAY",
        name: String = "Pilates Class",
        description: String = "Come join our pilates class today!",
        imageUrl: String = "",
        startTime: String = "07:00:00",
        endTime: String = "08:00:00",
        duration: Duration = Duration(
            value = 3600,
            unit = "SECONDS"
        )
    ): FitnessClass {
        return FitnessClass(
            dayOfWeek = dayOfWeek,
            name = name,
            description = description,
            imageUrl = imageUrl,
            startTime = startTime,
            endTime = endTime,
            duration = duration
        )
    }

    fun personalTrainers(
        trainers: ImmutableList<PersonalTrainer> = persistentListOf(
            createPersonalTrainer(
                firstName = "John",
                lastName = "Doe",
                bio = "Bio",
                imageUrl = IMAGE_URL
            ),
            createPersonalTrainer(
                firstName = "Jane",
                lastName = "Doe",
                bio = "Bio",
                imageUrl = IMAGE_URL
            ),
            createPersonalTrainer(
                firstName = "Jack",
                lastName = "Doe",
                bio = "Bio",
                imageUrl = IMAGE_URL
            )
        )
    ) = trainers

    private fun createPersonalTrainer(
        firstName: String = "John",
        lastName: String = "Doe",
        bio: String = "Bio",
        imageUrl: String = "",
        qualifications: List<String> = listOf("qualification1", "qualification2"),
        socials: Map<String, String> = mapOf("instagram" to "https://www.instagram.com", "tiktok" to "https://www.tiktok.com"),
        gymLocation: GymLocation = GymLocation.CLONTARF
    ): PersonalTrainer {
        return PersonalTrainer(
            firstName = firstName,
            lastName = lastName,
            bio = bio,
            imageUrl = imageUrl,
            qualifications = qualifications,
            socials = socials,
            gymLocation = gymLocation
        )
    }

    const val IMAGE_URL = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.libm.co.uk%2Fwp-content%2Fuploads%2F2017%2F09%2F35-Personal-Trainer-Fitness-Instructor-Course.jpg&f=1&nofb=1&ipt=4a7dd2591bf00e81d8ef2268a91853c3d8d7d4eee73c567d6230fd5a44711716&ipo=images"
}