I would like you to wire up the business logic from the shared module's fitness class feature to the web app UI fitness class. 

In the UI package, there is a `UpcomingClassesSection` composable that should display the list of fitness classes and their details.

Please consider the following when integrating the data:

- Remove the existing hardcoded list of fitness classes in the `UpcomingClassesSection` and replace it with dynamic data fetched from the shared module.
- Use the `suspend fun fetchUpcomingClasses(): Result<ImmutableList<FitnessClass>>` function from the shared module to retrieve the list of upcoming fitness classes.
- The `FitnessClass` data class has the following properties:

`data class FitnessClass(
val dayOfWeek: DayOfWeek,
val startTime: LocalTime,
val endTime: LocalTime,
val duration: Duration,
val name: String,
val description: String,
val imageUrl: String,
)`

Consider replacing the ClassCategory enum class and modifying the FitnessClassItem with the mapped data. 

Make sure to handle loading and error states appropriately in the UI while fetching the data. If any questions arise about the existing UI implementation or how to integrate the data, please ask for clarification before proceeding.