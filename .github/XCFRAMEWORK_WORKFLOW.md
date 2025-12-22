# XCFramework CI/CD Workflow Documentation

This document explains the GitHub Actions workflows for building and distributing the SharedGymPlanner XCFramework.

## Overview

The project includes three GitHub Actions workflows:

1. **validate-xcframework.yml** - Validates XCFramework builds on PRs
2. **build-xcframework.yml** - Builds and publishes XCFramework on releases
3. Manual build script for local development

## Workflows

### 1. Validate XCFramework Build

**File:** `.github/workflows/validate-xcframework.yml`

**Triggers:**
- Pull requests that modify `shared/**` or workflow files
- Manual workflow dispatch

**Purpose:**
Ensures that the XCFramework can be built successfully before merging changes.

**Steps:**
1. Builds device framework (iosArm64)
2. Builds simulator framework (iosSimulatorArm64)
3. Assembles XCFramework
4. Validates structure
5. Tests the build script
6. Uploads artifacts for inspection

### 2. Build and Export XCFramework

**File:** `.github/workflows/build-xcframework.yml`

**Triggers:**
- Push to `main` or `develop` branches
- Version tags (e.g., `v1.0.0`)
- Manual workflow dispatch

**Purpose:**
Builds the XCFramework and creates GitHub releases for distribution via Swift Package Manager.

**Steps:**
1. Builds XCFramework for all iOS targets
2. Creates zip archive
3. Calculates checksum for Swift Package Manager
4. Generates `Package.swift` manifest
5. Creates GitHub release with artifacts
6. Verifies package structure

**Outputs:**
- `SharedGymPlanner.xcframework.zip` - The XCFramework archive
- `Package.swift` - Swift Package manifest with checksum

## Local Development

### Building XCFramework Locally

Use the provided build script:

```bash
./build-xcframework.sh [version]
```

Example:
```bash
./build-xcframework.sh 1.0.0
```

This script:
1. Cleans previous builds
2. Builds the XCFramework
3. Creates a zip archive
4. Calculates the checksum
5. Generates `Package.swift` with the correct checksum

### Manual Gradle Commands

If you prefer to use Gradle directly:

```bash
# Clean
./gradlew clean

# Build device framework
./gradlew :shared:linkReleaseFrameworkIosArm64 --no-configuration-cache

# Build simulator framework
./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64 --no-configuration-cache

# Assemble XCFramework
./gradlew :shared:assembleSharedGymPlannerXCFramework --no-configuration-cache
```

**Note:** `--no-configuration-cache` is required due to KMP iOS framework compatibility issues.

## Release Process

### Creating a New Release

1. **Update version** in your project (if needed)

2. **Create and push a version tag:**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

3. **GitHub Actions will automatically:**
   - Build the XCFramework
   - Calculate checksums
   - Create a GitHub release
   - Attach the XCFramework zip and Package.swift

4. **iOS developers can then install via SPM:**
   ```swift
   dependencies: [
       .package(url: "https://github.com/IanArb/GymPlanner", from: "1.0.0")
   ]
   ```

### Manual Release (if needed)

If you need to create a release manually:

1. Build locally:
   ```bash
   ./build-xcframework.sh 1.0.0
   ```

2. Go to GitHub → Releases → Create new release

3. Upload:
   - `shared/build/XCFrameworks/release/SharedGymPlanner.xcframework.zip`
   - `shared/build/XCFrameworks/release/Package.swift`

4. Update the root `Package.swift` with the new checksum

## Troubleshooting

### Configuration Cache Issues

The workflows disable Gradle's configuration cache because it's incompatible with Kotlin Native iOS framework tasks:

```bash
./gradlew <task> --no-configuration-cache
```

Or add to `gradle.properties`:
```properties
org.gradle.configuration-cache=false
```

### XCFramework Not Found

If `xcodebuild` exits with code 70, the framework binaries weren't built:

1. Check that frameworks exist:
   ```bash
   ls -la shared/build/bin/iosArm64/releaseFramework/
   ls -la shared/build/bin/iosSimulatorArm64/releaseFramework/
   ```

2. Force rebuild:
   ```bash
   ./gradlew clean
   ./gradlew :shared:assembleSharedGymPlannerXCFramework --rerun-tasks --no-configuration-cache
   ```

### Workflow Fails on macOS Runner

Ensure your workflow uses `runs-on: macos-latest` because:
- `xcodebuild` command is only available on macOS
- Swift Package Manager tools require macOS

### Checksum Mismatch

If iOS developers report checksum errors:

1. Rebuild the XCFramework
2. Recalculate checksum:
   ```bash
   swift package compute-checksum SharedGymPlanner.xcframework.zip
   ```
3. Update `Package.swift` with the new checksum
4. Create a new release

## Architecture Support

The XCFramework includes slices for:

- **ios-arm64** - iOS devices (iPhone, iPad)
- **ios-arm64-simulator** - Apple Silicon Mac simulators
- **ios-x86_64-simulator** - Intel Mac simulators (via iosX64 target)

## Swift Package Manager Integration

The generated `Package.swift` uses a binary target:

```swift
.binaryTarget(
    name: "SharedGymPlanner",
    url: "https://github.com/IanArb/GymPlanner/releases/download/v1.0.0/SharedGymPlanner.xcframework.zip",
    checksum: "abc123..."
)
```

This allows iOS developers to integrate the framework via SPM without needing Kotlin or Gradle.

## Best Practices

1. **Always test locally** before pushing tags
2. **Use semantic versioning** (e.g., v1.0.0, v1.1.0)
3. **Validate with the validation workflow** on PRs
4. **Keep checksums in sync** between releases and Package.swift
5. **Document breaking changes** in release notes

## References

- [Kotlin Multiplatform Mobile Documentation](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)
- [Swift Package Manager Binary Targets](https://developer.apple.com/documentation/xcode/distributing-binary-frameworks-as-swift-packages)
- [XCFramework Documentation](https://developer.apple.com/documentation/xcode/creating-a-multi-platform-binary-framework-bundle)
- [GitHub Actions for iOS](https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-swift)

