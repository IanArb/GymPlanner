# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Module Overview

`:webApp` is the Kotlin/Wasm browser application. It targets `wasmJs` only and uses Compose Multiplatform for its UI. It depends on `:shared` for all business logic.

## Building

```bash
# Development build (outputs to build/dist/wasmJs/developmentExecutable/)
./gradlew :webApp:wasmJsBrowserDevelopmentExecutable

# Production build
./gradlew :webApp:wasmJsBrowserProductionExecutable

# Run in local dev server (with hot reload)
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

The Webpack output file is named `gymplanner.js` (configured in `build.gradle.kts`).

## Testing

There are currently no tests in this module.

## Architecture

### Entry Point

`src/wasmJsMain/kotlin/Main.kt` is the sole entry point. It uses `ComposeViewport` to mount the Compose UI tree into the browser DOM.

### Source Sets

| Source set | Path | Purpose |
|---|---|---|
| `wasmJsMain` | `src/wasmJsMain/kotlin/` | Application code |
| `commonMain` | `src/commonMain/kotlin/` | Shared Compose UI (currently unused) |
| `webMain/resources` | `src/webMain/resources/` | Static web assets |
| `webpack.config.d/` | `src/webpack.config.d/config.js` | Webpack customization |

### Dependencies

- **`:shared`** — KMP business logic and repositories
- **Compose Multiplatform** — `compose.runtime`, `compose.foundation`, `compose.material3` (Jetbrains variant), `compose.materialIconsCore`, `compose.components.resources` (when resource loading is needed)

UI components should be written as `@Composable` functions in `commonMain` where possible so they can eventually be shared with other Compose Multiplatform targets.