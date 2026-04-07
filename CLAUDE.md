# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

GymPlanner is a **Kotlin Multiplatform (KMP)** project targeting Android, iOS, and Web (Wasm). The shared module contains all business logic; platforms provide their own UI layers.

## Build & Development Commands

### Building

```bash
# Build shared module
./gradlew :shared:build

# Build Android app
./gradlew :androidApp:build

# Build web/Wasm app
./gradlew :webApp:build

# Build iOS XCFramework
./gradlew :shared:assembleSharedGymPlannerXCFramework
# Or via shell script (pass version):
./build-xcframework.sh 1.0.0
```

### Testing

```bash
# Run all tests
./gradlew test

# Run shared module tests
./gradlew :shared:test

# Run Android unit tests
./gradlew :androidApp:test

# Run a single test class
./gradlew :androidApp:testDebugUnitTest --tests "com.ianarbuckle.gymplanner.android.SomeTest"

# Roborazzi screenshot tests
./gradlew :androidApp:testDebugUnitTest

# Android instrumented tests
./gradlew :androidApp:connectedAndroidTest
```

### Code Quality

```bash
# Format code (ktfmt via Spotless)
./gradlew spotlessApply

# Check formatting without applying
./gradlew spotlessCheck

# Static analysis
./gradlew detekt
```

## Architecture

### Module Structure

- **`:shared`** — KMP module with `commonMain`, `androidMain`, `iosMain`, `wasmJsMain` source sets. Contains all business logic, data models, networking, and DI setup.
- **`:androidApp`** — Android-only UI layer using Jetpack Compose + Hilt.
- **`:webApp`** — Kotlin/Wasm web app with a single `Main.kt` entrypoint using Compose Multiplatform.
- **`iosApp/`** — Swift wrapper (not a Gradle module) that consumes the XCFramework.

### Dependency Injection Split

There are **two DI frameworks in use**:
- **Koin 4.x** — used in `:shared` (commonMain) for cross-platform DI
- **Hilt** — used in `:androidApp` for Android-specific DI; Hilt modules bridge into Koin where needed

### Networking

Ktor is the HTTP client across all platforms. Base URLs are injected as build config fields in `androidApp/build.gradle.kts`:
- `BASE_URL` — REST API (ngrok tunnel to local [GymPlannerService](https://github.com/IanArb/GymPlannerService))
- `WEBSOCKET_URL` — WebSocket endpoint for the chat feature

### Shared Feature Layout

Each feature in `shared/src/commonMain/kotlin/com/ianarbuckle/gymplanner/` typically contains:
- A repository interface + implementation
- Data models / DTOs (Kotlinx Serialization)
- Koin module declaration

Android feature modules under `androidApp/` follow a `presentation/` + `data/` + `di/` structure.

### iOS Distribution

The shared module is distributed to iOS as an XCFramework (`SharedGymPlanner.xcframework`) via Swift Package Manager. The `Package.swift` at the repo root points to a versioned release artifact. Use `build-xcframework.sh` or the CI workflow to produce a release.

## Key Technology Versions

Managed via `gradle/libs.versions.toml`:
- Kotlin: 2.3.10
- Compose Multiplatform: 1.10.3
- Ktor: 3.4.1
- Koin: 4.2.0
- Hilt: 2.59.2
- Detekt: 1.23.7

## Commit Conventions

This repo uses **Conventional Commits** for automated semantic versioning:
- `feat:` → minor version bump
- `fix:` → patch version bump
- `BREAKING CHANGE` in footer → major version bump

Run `npm run commit` for an interactive commit wizard (commitizen). CI enforces commit format via commitlint.