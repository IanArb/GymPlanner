---
name: android-ui-tests
description: Write Android instrumented UI tests for a feature following the Robot/Verifier pattern
allowed-tools: Read Glob Grep Write Edit
argument-hint: "[feature name]"
---

Write Android instrumented UI tests for $ARGUMENTS following the conventions in this project.

## File structure

Create these files under `androidApp/src/androidTest/kotlin/com/ianarbuckle/gymplanner/android/<feature>/`:

```
<feature>/
├── <Feature>InstrumentedTests.kt       # Test class
├── robot/<Feature>Robot.kt             # UI actions
├── verifier/<Feature>Verifier.kt       # UI assertions
├── fakes/Fake<Feature>Repository.kt    # Fake repository
└── di/Fake<Feature>Module.kt           # Hilt @TestInstallIn module
```

## 1. Fake repository

Simple implementation of the `:shared` repository interface with a `shouldReturnError` flag:

```kotlin
class Fake<Feature>Repository : <Feature>Repository {
    var shouldReturnError = false

    override suspend fun fetch<Feature>(): Result<...> {
        return if (shouldReturnError) {
            Result.failure(Exception("Error"))
        } else {
            Result.success(DataProvider.<feature>())
        }
    }
}
```

## 2. Hilt test module

Replaces the real Hilt module using `@TestInstallIn`:

```kotlin
@TestInstallIn(components = [SingletonComponent::class], replaces = [<Feature>Module::class])
@Module
class Fake<Feature>Module {
    @Singleton
    @Provides
    fun provide<Feature>Repository(): <Feature>Repository = Fake<Feature>Repository()
}
```

## 3. Robot

Encapsulates UI actions using semantic tags or `onNodeWithText`:

```kotlin
class <Feature>Robot(private val composeTestRule: ComposeTestRule) {
    fun tapOn<Feature>NavTab() {
        composeTestRule.onNodeWithText("Label").performClick()
    }
    fun perform<Action>() {
        composeTestRule.onNodeWithTag("<Tag>").performClick()
    }
}
```

## 4. Verifier

Encapsulates UI assertions:

```kotlin
class <Feature>Verifier(private val composeTestRule: ComposeTestRule) {
    fun verify<Feature>ScreenIsDisplayed() {
        composeTestRule.onNodeWithText("Title").assertIsDisplayed()
    }
    fun verifyErrorStateIsDisplayed() {
        composeTestRule.onNodeWithText("Error message").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tap to retry").assertIsDisplayed()
    }
    fun verifyItemsSize(size: Int) {
        composeTestRule.onNodeWithTag("<ListTag>").onChildren().assertCountEquals(size)
    }
}
```

## 5. Instrumented test class

```kotlin
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class <Feature>InstrumentedTests {

    @get:Rule(order = 1) val hiltTestRule = HiltAndroidRule(this)
    @get:Rule(order = 2) val composeTestRule = createAndroidComposeRule<MainActivity>()
    @get:Rule(order = 3)
    val postNotificationsPermissionRule =
        ConditionalPermissionRule(permission = Manifest.permission.POST_NOTIFICATIONS, minSdk = 33)

    private val testModule = module { single<DataStore<Preferences>> { FakeDataStore() } }
    @get:Rule val koinTestRule = KoinTestRule(modules = listOf(testModule))

    // Replace ViewModels via @BindValue — MockK stubs control state
    @BindValue @JvmField val <feature>ViewModel = mockk<<Feature>ViewModel>(relaxed = true)
    @BindValue @JvmField val dashboardViewModel = mockk<DashboardViewModel>(relaxed = true)

    // Access injected fake repository via cast
    @Inject lateinit var <feature>Repository: <Feature>Repository
    private val fake<Feature>Repository: Fake<Feature>Repository
        get() = <feature>Repository as Fake<Feature>Repository

    private val loginRobot = LoginRobot(composeTestRule)
    private val <feature>Robot = <Feature>Robot(composeTestRule)
    private val <feature>Verifier = <Feature>Verifier(composeTestRule)

    @Before
    fun setup() {
        hiltTestRule.inject()
    }

    @Test
    fun verify<Feature>SuccessState() {
        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile(),
            booking = DataProvider.bookings(),
        )

        coEvery { <feature>ViewModel.uiState.value } returns <Feature>UiState.Success(...)

        <feature>Robot.apply { tapOn<Feature>NavTab() }

        <feature>Verifier.apply {
            verify<Feature>ScreenIsDisplayed()
        }
    }

    @Test
    fun verify<Feature>ErrorState() {
        fake<Feature>Repository.shouldReturnError = true

        loginRobot.apply {
            enterUsernamePassword("test", "password")
            login()
        }

        coEvery { dashboardViewModel.uiState.value } returns DashboardUiState.Success(
            items = DataProvider.fitnessClasses(),
            profile = DataProvider.profile(),
            booking = DataProvider.bookings(),
        )

        coEvery { <feature>ViewModel.uiState.value } returns <Feature>UiState.Failure

        <feature>Robot.apply { tapOn<Feature>NavTab() }

        <feature>Verifier.apply {
            verify<Feature>ScreenIsDisplayed()
            verifyErrorStateIsDisplayed()
        }
    }
}
```

## Rules

- **Rule order matters** — always `order = 1` Hilt, `order = 2` Compose, `order = 3` permissions; `koinTestRule` has no order
- Always call `hiltTestRule.inject()` in `@Before`
- Use `@BindValue @JvmField` to replace ViewModels; use `coEvery` to stub `uiState.value`
- Use `@Inject` + cast to access the fake repository for controlling error state
- Robot methods use `performClick`, `performTextInput`; Verifier methods use `assertIsDisplayed`, `assertCountEquals`
- Always include both a success state test and a failure/error state test at minimum
- Read the real screen composable to find the correct semantic tags and text labels before writing assertions