# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Module Overview

`:androidApp` is the Android application module. It is a pure UI/presentation layer — all business logic lives in `:shared`. It uses Jetpack Compose with Material3 and targets minSdk 26 / compileSdk 36.

## Building

```bash
# Debug APK
./gradlew :androidApp:assembleDebug

# Release APK
./gradlew :androidApp:assembleRelease
```

`BASE_URL` and `WEBSOCKET_URL` are injected as `BuildConfig` fields in `build.gradle.kts`. They point to ngrok tunnels that proxy to the local [GymPlannerService](https://github.com/IanArb/GymPlannerService) backend — update these when the tunnel URLs change.

## Testing

```bash
# All unit tests (includes Roborazzi screenshot tests)
./gradlew :androidApp:testDebugUnitTest

# Run a single test class
./gradlew :androidApp:testDebugUnitTest --tests "gymplanner.booking.BookingViewModelTests"

# Instrumented tests (requires connected device/emulator)
./gradlew :androidApp:connectedAndroidTest

# Record new screenshot baselines
./gradlew :androidApp:recordRoborazziDebug
```

Note: `testReleaseUnitTest` is explicitly disabled (Roborazzi compatibility).

### Test Infrastructure

Shared test utilities live in `src/test/kotlin/gymplanner/utils/`:

| File | Purpose |
|---|---|
| `TestCoroutineRule.kt` | JUnit rule for `UnconfinedTestDispatcher` |
| `KoinTestRule.kt` | JUnit rule to start/stop a Koin context per test |
| `FakeDataStore.kt` | In-memory `DataStore<Preferences>` for tests |
| `RoborazziRule.kt` | Wrapper rule for Roborazzi screenshot capture |
| `ComposeTestRules.kt` | Factory for `AndroidComposeTestRule` |
| `PreviewUtils.kt` | `ScreenTestPreview` composable for light/dark previews |

### ViewModel Tests

- Construct the ViewModel directly with mocked dependencies (MockK).
- Use `app.cash.turbine` to assert `StateFlow` emissions in order.
- Use `TestCoroutineRule` as a `@get:Rule`.

### Screenshot Tests

- Run with Robolectric (`@RunWith(AndroidJUnit4::class)`, `@GraphicsMode(NATIVE)`, `@Config(sdk = [34])`).
- Reference images are stored in `src/screenshots/`.
- Dark-mode variants use `@Config(qualifiers = "+night")`.
- Provide a `KoinTestRule` with a `testModule` that supplies fakes (e.g. `FakeDataStore`) for any Koin dependencies the composable needs.

### Instrumented Tests

Instrumented tests live in `src/androidTest/` and run on a real device or emulator against `MainActivity`.

**Test runner:** `CustomTestRunner` — swaps the application class for `HiltTestApplication` so Hilt can inject into test classes.

**Rule order matters** — rules must be declared with explicit `order` values:

```kotlin
@get:Rule(order = 1) val hiltTestRule = HiltAndroidRule(this)
@get:Rule(order = 2) val composeTestRule = createAndroidComposeRule<MainActivity>()
@get:Rule(order = 3) val permissionRule = ConditionalPermissionRule(...)
@get:Rule         val koinTestRule = KoinTestRule(listOf(testModule))
```

Always call `hiltTestRule.inject()` in `@Before`.

**Shared androidTest utilities** in `src/androidTest/.../utils/`:

| File | Purpose |
|---|---|
| `CustomTestRunner.kt` | Replaces app class with `HiltTestApplication` |
| `HiltTestApplication.kt` | Hilt-compatible test application |
| `KoinTestRule.kt` | Starts/stops Koin with test modules |
| `FakeDataStore.kt` | In-memory `DataStore<Preferences>` |
| `DisableAnimationsRule.kt` | Disables Compose animations for stable assertions |
| `ComposeIdlingResource.kt` | Espresso idling resource for Compose |
| `PermissionRule.kt` / `ConditionalPermissionRule.kt` | Grants runtime permissions (e.g. `POST_NOTIFICATIONS`) conditionally by SDK level |

**Fake Hilt modules** replace real modules at test time using `@TestInstallIn`:

```kotlin
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [DashboardModule::class])
class FakeDashboardModule {
    @Singleton @Provides
    fun providesFitnessClassesRepository(): FitnessClassRepository = FakeFitnessClassRepository()
}
```

Each feature has its own `Fake<Feature>Module` in `androidTest/.../di/` and corresponding fake repository implementations in `androidTest/.../fakes/`.

**Robot/Verifier pattern** — tests are structured into three classes per feature:

- **Robot** (`robot/`) — performs UI actions (`performClick`, `performTextInput`) using semantic tags.
- **Verifier** (`verifier/`) — asserts visible state (`assertIsDisplayed`, `onNodeWithText`).
- **InstrumentedTests** — orchestrates robots and verifiers, stubs ViewModel state via MockK `@BindValue`.

ViewModels can be replaced entirely in instrumented tests using Hilt's `@BindValue`:

```kotlin
@BindValue @JvmField val viewModel = mockk<DashboardViewModel>(relaxed = true)
```

Then stub the `uiState` before assertions:

```kotlin
coEvery { viewModel.uiState.value } returns DashboardUiState.Success(...)
```

## Architecture

### MVVM Pattern

The app follows MVVM (Model–View–ViewModel) with unidirectional data flow:

```
View (Composable)
    │  observes StateFlow
    ▼
ViewModel (@HiltViewModel)
    │  calls suspend functions / Result<T>
    ▼
Repository (interface from :shared)
    │
    ▼
RemoteDataSource (Ktor, in :shared)
```

**Model** — domain models and DTOs live in `:shared`. The ViewModel maps them into UI state; Composables never use raw domain objects directly.

**ViewModel** — annotated `@HiltViewModel`, injected via `@Inject constructor`. Holds a private `MutableStateFlow` exposed as a read-only `StateFlow`:

```kotlin
private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
val uiState = _uiState.asStateFlow()
```

State transitions follow `Idle → Loading → Success | Failure`. Parallel async work uses `supervisorScope` + `async`/`await` so one failing call does not cancel others.

**UiState** — a `sealed interface` with four standard variants:

```kotlin
sealed interface DashboardUiState {
    data class Success(...) : DashboardUiState
    data object Failure     : DashboardUiState
    data object Loading     : DashboardUiState
    data object Idle        : DashboardUiState
}
```

**View** — Composables collect state with `collectAsStateWithLifecycle()` and render based on the current `UiState` variant. User actions call ViewModel functions directly; there is no separate Action/Intent class unless the feature has multiple distinct user intents.

### Feature Package Structure

```
<feature>/
├── <Feature>ViewModel.kt       # @HiltViewModel, owns StateFlow<UiState>
├── <Feature>UiState.kt         # sealed interface with Idle/Loading/Success/Failure
├── <Feature>Action.kt          # (optional) distinct user intent events
├── presentation/               # Composable screens and components
└── di/
    └── <Feature>Module.kt      # Hilt module scoped to ViewModelComponent
```

### Dependency Injection

Two DI frameworks are active simultaneously:

- **Hilt** — manages Android-layer dependencies (ViewModels, Android platform types). `GymPlannerApplication` is annotated `@HiltAndroidApp`.
- **Koin** — initialized inside `GymPlannerApplication.onCreate()` via `initKoin()` from `:shared`. Provides all repository and network dependencies.

App-wide Hilt modules are in `di/`:
- `UrlModule.kt` — provides `BASE_URL` and `WEBSOCKET_URL` as `@Named` strings
- `StorageModule.kt` — provides `DataStoreRepository`
- `GymPlannerModule.kt` — provides `Clock.System`
- `FcmModule.kt` — provides FCM-related bindings

Feature Hilt modules are scoped to `ViewModelComponent` and provide repository implementations sourced from `:shared`.

### Navigation

Navigation uses `androidx.navigation3` (the new type-safe Navigation 3 library). Entry points and route definitions are in the `navigation/` package.

### Compose Compiler Reports

The Compose compiler is configured to emit stability/metrics reports to `build/compose_compiler/`. Run any build task to regenerate them.