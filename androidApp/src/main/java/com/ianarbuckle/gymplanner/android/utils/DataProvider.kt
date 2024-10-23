package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.clients.domain.Client
import com.ianarbuckle.gymplanner.clients.domain.GymPlan
import com.ianarbuckle.gymplanner.clients.domain.PersonalTrainer
import com.ianarbuckle.gymplanner.clients.domain.Session
import com.ianarbuckle.gymplanner.clients.domain.Weight
import com.ianarbuckle.gymplanner.clients.domain.Workout
import com.ianarbuckle.gymplanner.fitnessclass.domain.Duration
import com.ianarbuckle.gymplanner.fitnessclass.domain.FitnessClass
import com.ianarbuckle.gymplanner.gymlocations.domain.GymLocations
import com.ianarbuckle.gymplanner.personaltrainers.domain.GymLocation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random

object DataProvider {

    fun client(
        id: String = Random.nextInt().toString(),
        firstName: String = "Pablo",
        surname: String = "Escobar",
        strength: String = "advanced",
        name: String = "December Strength Training",
        startDate: String = "2022-03-20T15:00",
        endDate: String = "2022-04-20T15:00",
        sessions: List<Session> = sessions()
    ) = Client(
        firstName = firstName,
        surname = surname,
        strengthLevel = strength,
        gymPlan = gymPlan(
            name = name,
            startDate = startDate,
            endDate = endDate,
            sessions = sessions
        )
    )

    fun gymPlan(
        name: String = "December Strength Training",
        startDate: String = "2022-03-20T15:00",
        endDate: String = "2022-04-20T15:00",
        sessions: List<Session> = emptyList()
    ): GymPlan {
        return GymPlan(
            name = name,
            personalTrainer = personalTrainer(),
            startDate = startDate,
            endDate = endDate,
            sessions = sessions
        )
    }

    private fun personalTrainer(
        firstName: String = "Vini",
        lastName: String = "Feynda",
        bio: String = "Hey, I'm Vini. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl: String = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials: Map<String, String> = mapOf()
    ) = PersonalTrainer(
        firstName = firstName,
        lastName = lastName,
        bio = bio,
        imageUrl = imageUrl,
        socials = socials,
        gymLocation = GymLocation.CLONTARF,
    )

    fun sessions(): List<Session> {
        return listOf(
            Session(
                "Chest and Triceps",
                listOf(
                    Workout(
                        name = "Bench Press",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 25.0, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Decline Bench Press",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Cable Push down",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Cluster set"
                    ),
                    Workout(
                        name = "Triceps overhead extensions",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Cluster set"
                    ),
                    Workout(
                        name = "Single arm kick back",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Cluster set"
                    )
                )
            ),
            Session(
                "Back and Biceps",
                listOf(
                    Workout(
                        name = "Lat Pull down",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Wide grip"
                    ),
                    Workout(
                        name = "Lat Pull down",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Machine (cluster)"
                    ),
                    Workout(
                        name = "Dumbbell row",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Lower extension",
                        sets = 3,
                        repetitions = 15,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Concentration curl",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Cable curl",
                        sets = 3,
                        repetitions = 15,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    )
                )
            ),
            Session(
                "Legs",
                listOf(
                    Workout(
                        name = "Squat machine",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Good morning",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Leg press",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Hamstring curls",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Leg Extensions single",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Lunges",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    )
                )
            ),
            Session(
                "Shoulders",
                listOf(
                    Workout(
                        name = "Shoulder Press (Machine)",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Lateral Raise cable",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    )
                )
            ),
            Session(
                "Chest and Triceps",
                listOf(
                    Workout(
                        name = "Chest fly",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Machine"
                    ),
                    Workout(
                        name = "Dumbbell pullover",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Smith machine chest press",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Triceps dip",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Cable over head & bench dip combo",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    )
                )
            ),
            Session(
                "Legs",
                listOf(
                    Workout(
                        name = "Sumo squat",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Split squat",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Machine"
                    ),
                    Workout(
                        name = "Low bar squat",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Hip thrust",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = ""
                    ),
                    Workout(
                        name = "Calves push up",
                        sets = 4,
                        repetitions = 10,
                        weight = Weight(value = 12.5, unit = "kg"),
                        note = "Machine"
                    )
                )
            ),
        )
    }

    fun fitnessClasses(): ImmutableList<FitnessClass> {
        return persistentListOf(
            FitnessClass(
                dayOfWeek = "MONDAY",
                name = "Pilates Class",
                description = "Come join our pilates class today!",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
                startTime = "",
                endTime = "",
                duration = Duration(
                    value = 0,
                    unit = "SECONDS"
                )
            ),
            FitnessClass(
                dayOfWeek = "MONDAY",
                name = "Strength training",
                description = "We will be focusing on lower body today!",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
                startTime = "",
                endTime = "",
                duration = Duration(
                    value = 0,
                    unit = "SECONDS"
                )
            ),
            FitnessClass(
                dayOfWeek = "MONDAY",
                name = "Body pump",
                description = "Come join us for body bump!",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
                startTime = "",
                endTime = "",
                duration = Duration(
                    value = 0,
                    unit = "SECONDS"
                )
            )
        )
    }

    fun gymLocations(): List<GymLocations> {
        return listOf(
            GymLocations(
                title = "Clontarf",
                subTitle = "Personal Trainers Dublin 3",
                description = "Clontarf is a beautiful location with a great view of the sea.",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
            ),
            GymLocations(
                title = "Aston Quay",
                subTitle = "Personal Trainers Dublin 2",
                description = "Aston Quay where the Brits invaded Ireland",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
            ),
            GymLocations(
                title = "Leopardstown",
                subTitle = "Personal Trainers Dublin 18",
                description = "Leopardstown? Where the races happen!",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
            ),
            GymLocations(
                title = "Westmantown",
                subTitle = "Personal Trainers Dublin 15",
                description = "Westmantown? Who cares!",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
            ),
            GymLocations(
                title = "Sandymount",
                subTitle = "Personal Trainers Dublin 4",
                description = "Where the posh people live",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
            ),
            GymLocations(
                title = "Dun Laoghaire",
                subTitle = "Personal Trainers Dublin 4",
                description = "Where the posh people live",
                imageUrl = "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
            ),
        )
    }
}