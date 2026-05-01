I would like you to use the shared module's personal trainers schedule feature to create a web app UI that allows users to view and manage their personal training schedules. 

* The Ui already has the personal trainers schedule feature implemented, it just needs the data to be wired up and displayed.
* The shared module exposes the  `suspend fun fetchTrainerSchedules(date: String): Result<ImmutableList<PersonalTrainer>>` function that returns a list of personal trainers available on a given date.
* The date format is "YYYY-MM-DD" (e.g., "2024-07-01").
* The `PersonalTrainer` data class has the following properties:
  - `id: String`
  - `name: String`
  - `availableTimes: ImmutableList<String>` (list of available time slots in "HH:mm" format)

* The web app UI is located in trainers package and consists of a `TrainersScheduleScreen` composable that should display the list of personal trainers and their available time slots for a selected date.

You should not rewrite the existing UI code, but rather focus on integrating the data from the shared module into the `TrainersScheduleScreen`. This will involve calling the `fetchTrainerSchedules` function with the selected date, handling the result, and updating the UI accordingly to display the trainers and their schedules.

If any questions arise about the existing UI implementation or how to integrate the data, please ask for clarification before proceeding.