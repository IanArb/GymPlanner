package com.ianarbuckle.gymplanner.android.utils

import com.ianarbuckle.gymplanner.availability.domain.Availability
import com.ianarbuckle.gymplanner.availability.domain.Slot
import com.ianarbuckle.gymplanner.availability.domain.Time
import com.ianarbuckle.gymplanner.booking.domain.Booking
import com.ianarbuckle.gymplanner.booking.domain.BookingResponse
import com.ianarbuckle.gymplanner.booking.domain.BookingStatus
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
import com.ianarbuckle.gymplanner.profile.domain.Profile
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.LocalTime

object DataProvider {

  fun client(
    firstName: String = "Pablo",
    surname: String = "Escobar",
    strength: String = "advanced",
    name: String = "December Strength Training",
    startDate: String = "2022-03-20T15:00",
    endDate: String = "2022-04-20T15:00",
    sessions: List<Session> = sessions(),
  ) =
    Client(
      firstName = firstName,
      surname = surname,
      strengthLevel = strength,
      gymPlan = gymPlan(name = name, startDate = startDate, endDate = endDate, sessions = sessions),
    )

  fun gymPlan(
    name: String = "December Strength Training",
    startDate: String = "2022-03-20T15:00",
    endDate: String = "2022-04-20T15:00",
    sessions: List<Session> = emptyList(),
  ): GymPlan {
    return GymPlan(
      name = name,
      personalTrainer = personalTrainer(),
      startDate = startDate,
      endDate = endDate,
      sessions = sessions,
    )
  }

  private fun personalTrainer(
    firstName: String = "Vini",
    lastName: String = "Feynda",
    bio: String =
      "Hey, I'm Vini. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
    imageUrl: String =
      "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
    socials: Map<String, String> = mapOf(),
  ) =
    PersonalTrainer(
      firstName = firstName,
      lastName = lastName,
      bio = bio,
      imageUrl = imageUrl,
      socials = socials,
      qualifications = listOf("Level 3 Personal Trainer", "Nutritionist"),
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
            note = "",
          ),
          Workout(
            name = "Decline Bench Press",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Cable Push down",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Cluster set",
          ),
          Workout(
            name = "Triceps overhead extensions",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Cluster set",
          ),
          Workout(
            name = "Single arm kick back",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Cluster set",
          ),
        ),
      ),
      Session(
        "Back and Biceps",
        listOf(
          Workout(
            name = "Lat Pull down",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Wide grip",
          ),
          Workout(
            name = "Lat Pull down",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Machine (cluster)",
          ),
          Workout(
            name = "Dumbbell row",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Lower extension",
            sets = 3,
            repetitions = 15,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Concentration curl",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Cable curl",
            sets = 3,
            repetitions = 15,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
        ),
      ),
      Session(
        "Legs",
        listOf(
          Workout(
            name = "Squat machine",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Good morning",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Leg press",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Hamstring curls",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Leg Extensions single",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Lunges",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
        ),
      ),
      Session(
        "Shoulders",
        listOf(
          Workout(
            name = "Shoulder Press (Machine)",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Lateral Raise cable",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
        ),
      ),
      Session(
        "Chest and Triceps",
        listOf(
          Workout(
            name = "Chest fly",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Machine",
          ),
          Workout(
            name = "Dumbbell pullover",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Smith machine chest press",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Triceps dip",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Cable over head & bench dip combo",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
        ),
      ),
      Session(
        "Legs",
        listOf(
          Workout(
            name = "Sumo squat",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Split squat",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Machine",
          ),
          Workout(
            name = "Low bar squat",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Hip thrust",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "",
          ),
          Workout(
            name = "Calves push up",
            sets = 4,
            repetitions = 10,
            weight = Weight(value = 12.5, unit = "kg"),
            note = "Machine",
          ),
        ),
      ),
    )
  }

  fun fitnessClasses(): ImmutableList<FitnessClass> {
    return persistentListOf(
      FitnessClass(
        dayOfWeek = "MONDAY",
        name = "Pilates Class",
        description = "Come join our pilates class today!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "16:00",
        endTime = "17:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "MONDAY",
        name = "Strength training",
        description = "We will be focusing on lower body today!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "17:00",
        endTime = "18:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "TUESDAY",
        name = "Body pump",
        description = "Come join us for body bump!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "19:00",
        endTime = "20:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "WEDNESDAY",
        name = "Body pump",
        description = "Come join us for body bump!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "19:00",
        endTime = "20:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "THURSDAY",
        name = "Body pump",
        description = "Come join us for body bump!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "19:00",
        endTime = "20:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "FRIDAY",
        name = "Body pump",
        description = "Come join us for body bump!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "19:00",
        endTime = "20:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "SATURDAY",
        name = "Body pump",
        description = "Come join us for body bump!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "19:00",
        endTime = "20:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
      FitnessClass(
        dayOfWeek = "SUNDAY",
        name = "Body pump",
        description = "Come join us for body bump!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=http%3A%2F%2Fwww.pilatesplusphysio.co.uk%2Fwp-content%2Fuploads%2Fbfi_thumb%2FIMG_7959-2-33jbpdyt71dr22dxnj3oxs.jpg",
        startTime = "19:00",
        endTime = "20:00",
        duration = Duration(value = 0, unit = "SECONDS"),
      ),
    )
  }

  fun gymLocations(): ImmutableList<GymLocations> {
    return persistentListOf(
      GymLocations(
        title = "Clontarf",
        subTitle = "Personal Trainers Dublin 3",
        description = "Clontarf is a beautiful location with a great view of the sea.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
      ),
      GymLocations(
        title = "Aston Quay",
        subTitle = "Personal Trainers Dublin 2",
        description = "Aston Quay where the Brits invaded Ireland",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
      ),
      GymLocations(
        title = "Leopardstown",
        subTitle = "Personal Trainers Dublin 18",
        description = "Leopardstown? Where the races happen!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
      ),
      GymLocations(
        title = "Westmantown",
        subTitle = "Personal Trainers Dublin 15",
        description = "Westmantown? Who cares!",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
      ),
      GymLocations(
        title = "Sandymount",
        subTitle = "Personal Trainers Dublin 4",
        description = "Where the posh people live",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
      ),
      GymLocations(
        title = "Dun Laoghaire",
        subTitle = "Personal Trainers Dublin 4",
        description = "Where the posh people live",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
      ),
    )
  }

  fun personalTrainers(): PersistentList<PersonalTrainer> {
    val socials =
      mapOf(
        "instagram" to "https://www.instagram.com/vinifeynda",
        "tiktok" to "https://www.tiktok.com/@crash.gear",
      )
    return persistentListOf(
      PersonalTrainer(
        firstName = "Vini",
        lastName = "Feynda",
        bio =
          "Hey, I'm Vini. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials = socials,
        qualifications = listOf("Level 3 Personal Trainer", "Nutritionist"),
        gymLocation = GymLocation.CLONTARF,
      ),
      PersonalTrainer(
        firstName = "Sinead",
        lastName = "Darcy",
        bio =
          "Hey, I'm Sinead. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials = socials,
        qualifications =
          listOf(
            "LesMillis BodyAttack",
            "LesMillis BodyPump",
            "LesMillis The Trip",
            "LesMillis BodyBalance",
            "LesMillis Dance",
            "LesMillis Sprint",
            "LesMillis BodyCombat",
          ),
        gymLocation = GymLocation.CLONTARF,
      ),
      PersonalTrainer(
        firstName = "Aine",
        lastName = "Darcy",
        bio =
          "Hey, I'm Aine. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials = mapOf(),
        qualifications = listOf("Level 3 Personal Trainer", "Nutritionist"),
        gymLocation = GymLocation.CLONTARF,
      ),
      PersonalTrainer(
        firstName = "John",
        lastName = "Doe",
        bio =
          "Hey, I'm John. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials = mapOf(),
        qualifications = listOf("Level 3 Personal Trainer", "Nutritionist"),
        gymLocation = GymLocation.ASTONQUAY,
      ),
      PersonalTrainer(
        firstName = "Jane",
        lastName = "Doe",
        bio =
          "Hey, I'm Jane. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials = mapOf(),
        qualifications =
          listOf(
            "LesMillis BodyAttack",
            "LesMillis BodyPump",
            "LesMillis The Trip",
            "LesMillis BodyBalance",
            "LesMillis Dance",
            "LesMillis Sprint",
            "LesMillis BodyCombat",
          ),
        gymLocation = GymLocation.LEOPARDSTOWN,
      ),
      PersonalTrainer(
        firstName = "Alice",
        lastName = "Doe",
        bio =
          "Hey, I'm Alice. I'm a personal trainer with over 10 years of experience. I'm here to help you achieve your fitness goals.",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        socials = mapOf(),
        qualifications = listOf("Level 3 Personal Trainer", "Nutritionist"),
        gymLocation = GymLocation.WESTMANSTOWN,
      ),
    )
  }

  val daysOfWeek: ImmutableList<String> =
    persistentListOf(
      "2024-12-08",
      "2024-12-09",
      "2024-12-10",
      "2024-12-11",
      "2024-12-12",
      "2024-12-13",
      "2024-12-14",
    )

  val availableTimes: List<String> =
    listOf(
      "09:00:00",
      "10:00:00",
      "10:30:00",
      "11:00:00",
      "12:00:00",
      "12:30:00",
      "13:00:00",
      "14:00:00",
      "14:30:00",
      "15:00:00",
      "16:00:00",
      "16:30:00",
    )

  fun profile(): Profile = Profile("123", "ianarbuckle", "Ian", "Arbuckle", "ian@mail.com")

  fun availability(personalTrainerId: String = "123", month: String = "2024-12-12"): Availability {
    return Availability(
      id = "123",
      month = month,
      personalTrainerId = personalTrainerId,
      slots =
        persistentListOf(
          Slot(
            date = "2024-12-08",
            id = "123",
            times =
              persistentListOf(
                Time(id = "1", endTime = "06:30:00", startTime = "06:00:00", status = "AVAILABLE"),
                Time(id = "2", endTime = "07:00:00", startTime = "06:30:00", status = "AVAILABLE"),
                Time(id = "3", endTime = "07:30:00", startTime = "07:00:00", status = "AVAILABLE"),
                Time(id = "4", endTime = "08:00:00", startTime = "07:30:00", status = "AVAILABLE"),
                Time(id = "5", endTime = "08:30:00", startTime = "08:00:00", status = "AVAILABLE"),
                Time(id = "6", endTime = "09:00:00", startTime = "08:30:00", status = "AVAILABLE"),
                Time(id = "7", endTime = "09:30:00", startTime = "09:00:00", status = "AVAILABLE"),
                Time(id = "8", endTime = "10:00:00", startTime = "09:30:00", status = "AVAILABLE"),
                Time(id = "9", endTime = "10:30:00", startTime = "10:00:00", status = "AVAILABLE"),
                Time(id = "10", endTime = "11:00:00", startTime = "10:30:00", status = "AVAILABLE"),
                Time(id = "11", endTime = "11:30:00", startTime = "11:00:00", status = "AVAILABLE"),
                Time(id = "12", endTime = "12:00:00", startTime = "11:30:00", status = "AVAILABLE"),
              ),
          )
        ),
    )
  }

  fun createBooking(
    timeSlotId: String = "1",
    userId: String = "1",
    personalTrainer: com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer =
      com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer(
        id = "123",
        name = "John Doe",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        gymLocation = GymLocation.CLONTARF,
      ),
  ): Booking {
    return Booking(
      timeSlotId = timeSlotId,
      userId = userId,
      personalTrainer = personalTrainer,
      startTime = LocalTime.parse("12:00:00"),
      bookingDate = "",
    )
  }

  fun bookingResponse(
    userId: String = "1",
    timeSlotId: String = "1",
    bookingDate: String = "2024-12-12",
    bookingTime: String = "12:00:00",
    personalTrainer: com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer =
      com.ianarbuckle.gymplanner.booking.domain.PersonalTrainer(
        id = "123",
        name = "John Doe",
        imageUrl =
          "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.verywellfit.com%2Fthmb%2FQ6Z",
        gymLocation = GymLocation.CLONTARF,
      ),
  ): BookingResponse {
    return BookingResponse(
      userId = userId,
      timeSlotId = timeSlotId,
      bookingDate = bookingDate,
      startTime = bookingTime,
      personalTrainer = personalTrainer,
      status = BookingStatus.CONFIRMED,
    )
  }

  fun bookings(): ImmutableList<BookingResponse> {
    return persistentListOf(bookingResponse())
  }
}
