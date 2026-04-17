# AGENTS.md

Quick reference for AI agents working in this repo. Prefer reading code over re-deriving what's here.

## Module Boundaries

| Module | Purpose | Off-limits |
|---|---|---|
| `:shared` | All business logic, repos, DTOs, Koin DI | No Android/iOS/platform imports |
| `:androidApp` | Compose UI + Hilt only | No business logic |
| `:webApp` | Compose/Wasm UI only | No business logic |
| `iosApp/` | Swift wrapper — not a Gradle module | Don't edit unless touching Swift |

## Essential Commands

```bash
./gradlew :shared:test                   # shared tests
./gradlew :androidApp:testDebugUnitTest  # Android unit + screenshot tests
./gradlew spotlessApply                  # format (run before committing)
./gradlew detekt                         # static analysis
```

## DI — Two Frameworks, One App

- **Koin 4.x** — `:shared` only. Module declarations in `shared/src/commonMain/.../di/Koin.kt`.
- **Hilt** — `:androidApp` only. Feature modules in `<feature>/di/<Feature>Module.kt`, scoped to `ViewModelComponent`.
- Hilt modules bridge into Koin by calling `get()` from Koin inside `@Provides` functions.

## Shared Feature Conventions

New features in `:shared` need:
1. `<Feature>RemoteDataSource` interface + `Default` impl
2. `<Feature>Repository` interface returning `Result<T>` + `Default` impl using `runCatching { }.onFailure { if (it is CancellationException) throw it }`
3. Koin module wired into `di/Koin.kt`

Android features need: `<Feature>ViewModel` (`@HiltViewModel`) + `<Feature>UiState` (sealed interface: `Idle/Loading/Success/Failure`) + Hilt module.

## Test Patterns

**Shared (`commonTest`)** — no Koin, no mocking framework. Use fakes:
- `Fake<Feature>RemoteDataSource` — configurable responses + call capture + `reset()`
- `Fake<Feature>Repository` — thin wrapper delegating to fake data source
- `<Feature>TestDataProvider` — all test data in one `object`

**Android unit tests** — MockK + Turbine for StateFlow assertions, `TestCoroutineRule`.

**Do not** mock `CancellationException` — always rethrow it in catch blocks.

## Commits

Conventional Commits are enforced by CI:
- `feat:` minor bump · `fix:` patch bump · `BREAKING CHANGE` footer → major bump
- Run `npm run commit` for the interactive wizard.