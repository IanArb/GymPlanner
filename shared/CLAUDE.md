# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Module Overview

`:shared` is the Kotlin Multiplatform library module. It contains all business logic, data models, networking, and DI configuration shared across Android, iOS, and Web targets.

**Targets:** `androidTarget`, `iosX64`, `iosArm64`, `iosSimulatorArm64`, `wasmJs`

## Building

```bash
# Build all targets
./gradlew :shared:build

# Build iOS XCFramework (all slices)
./gradlew :shared:assembleSharedGymPlannerXCFramework

# Build only the Android variant
./gradlew :shared:assembleDebug
```

## Testing

Tests live in `src/commonTest/`. There are currently no platform-specific test source sets.

```bash
# Run all shared tests
./gradlew :shared:test

# Run a specific test class
./gradlew :shared:testDebugUnitTest --tests "com.ianarbuckle.gymplanner.SomeTest"
```

**Test dependencies:** `kotlin-test`, `JUnit 4`, `Kotest assertions`, `kotlinx-coroutines-test`

## Code Quality

```bash
# Format (ktfmt, Kotlinlang style)
./gradlew :shared:spotlessApply

# Static analysis (config at /config/detekt.yml)
./gradlew :shared:detekt
```

## Architecture

### Package Structure

Each feature lives in its own package under `src/commonMain/kotlin/com/ianarbuckle/gymplanner/`:

```
<feature>/
├── <Feature>Repository.kt          # Interface
├── Default<Feature>Repository.kt   # Implementation (uses KoinComponent)
├── <Feature>RemoteDataSource.kt    # Interface
├── Default<Feature>RemoteDataSource.kt
├── domain/                         # Domain models
└── dto/                            # Serializable DTOs (Kotlinx Serialization)
```

### Dependency Injection

All DI configuration is in `di/Koin.kt`. `initKoin()` is the single entry point called by every platform:

- Each feature has its own module function (e.g. `bookingModule(baseUrl)`).
- `platformModule` is an `expect/actual` declaration in `di/PlatformModule.kt` — provides the platform-specific Ktor HTTP engine and DataStore path.
- Repositories use `KoinComponent` + `by inject()` to pull dependencies from the Koin container.

**iOS** calls the convenience wrapper `initKoinIOS(baseUrl, websocketBaseUrl)` from Swift.

### Networking

Ktor `HttpClient` is configured in `networkModule()` inside `di/Koin.kt`. All remote data sources receive the client and `baseUrl` as constructor parameters. WebSocket support (`ChatSocketService`) also receives a separate `websocketBaseUrl`.

### Platform-Specific Source Sets

| Source set | Purpose |
|---|---|
| `androidMain` | OkHttp engine, Android DataStore path |
| `iosMain` | Darwin engine, iOS DataStore path |
| `wasmJsMain` | Webpack output config only — no business logic |

### iOS Distribution

The module is packaged as `SharedGymPlanner.xcframework` (static framework). It is distributed via Swift Package Manager — see `shared/SWIFT_PACKAGE.md` and `Package.swift` at the repo root.