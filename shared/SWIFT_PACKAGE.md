# SharedGymPlanner Swift Package

This document describes how to integrate the SharedGymPlanner Kotlin Multiplatform framework into your iOS project using Swift Package Manager.

## Installation

### Via Swift Package Manager (Recommended)

#### Option 1: Xcode UI

1. In Xcode, go to **File → Add Package Dependencies**
2. Enter the repository URL: `https://github.com/IanArb/GymPlanner`
3. Select the version rule (e.g., "Up to Next Major Version" from `1.0.0`)
4. Click **Add Package**
5. Select **SharedGymPlanner** and click **Add Package**

#### Option 2: Package.swift

Add the following to your `Package.swift` file:

```swift
// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "YourApp",
    platforms: [
        .iOS(.v14)
    ],
    dependencies: [
        .package(url: "https://github.com/IanArb/GymPlanner", from: "1.0.0")
    ],
    targets: [
        .target(
            name: "YourApp",
            dependencies: [
                .product(name: "SharedGymPlanner", package: "GymPlanner")
            ]
        )
    ]
)
```

### Manual Installation

Download the latest `SharedGymPlanner.xcframework.zip` from the [releases page](https://github.com/IanArb/GymPlanner/releases), unzip it, and drag the `.xcframework` into your Xcode project.

## Usage

Import the framework in your Swift files:

```swift
import SharedGymPlanner
```

### Example Usage

```swift
import SwiftUI
import SharedGymPlanner

struct ContentView: View {
    @StateObject private var viewModel = YourViewModel()
    
    var body: some View {
        // Use the shared Kotlin code
        Text("Gym Planner")
    }
}

class YourViewModel: ObservableObject {
    // Initialize Koin or access shared repositories
    init() {
        // Example: Initialize your KMP modules
    }
}
```

## Building Locally

If you want to build the XCFramework locally:

### Prerequisites

- macOS with Xcode installed
- JDK 17 or higher
- Kotlin 2.2.21 or higher

### Build Commands

```bash
# Clone the repository
git clone https://github.com/IanArb/GymPlanner.git
cd GymPlanner

# Build the XCFramework
./gradlew :shared:assembleSharedGymPlannerXCFramework

# The XCFramework will be available at:
# shared/build/XCFrameworks/release/SharedGymPlanner.xcframework
```

### Create Swift Package Locally

```bash
cd shared/build/XCFrameworks/release

# Zip the XCFramework
zip -r SharedGymPlanner.xcframework.zip SharedGymPlanner.xcframework

# Calculate checksum
swift package compute-checksum SharedGymPlanner.xcframework.zip

# Create Package.swift with the checksum
```

## Supported Platforms

- iOS 14.0+
- iOS Simulator (arm64, x86_64)

## Architecture

The XCFramework includes binaries for:
- `ios-arm64` (iOS devices)
- `ios-arm64-simulator` (Apple Silicon Simulators)
- `ios-x86_64-simulator` (Intel Simulators)

## Release Process

Releases are automatically created when a version tag is pushed:

```bash
# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

The GitHub Actions workflow will:
1. Build the XCFramework for all iOS targets
2. Generate the Swift Package manifest with checksum
3. Create a GitHub release with the XCFramework attached
4. Make it available via Swift Package Manager

## Troubleshooting

### "Unable to resolve package"

Ensure you have the correct repository URL and that the release exists.

### "Failed to extract binary artifact"

The XCFramework may be corrupted. Try downloading a fresh copy from the releases page.

### Build errors with Kotlin code

Make sure your Xcode project's minimum deployment target is set to iOS 14.0 or higher.

### Koin initialization issues

Ensure you initialize Koin in your iOS app:

```swift
import SharedGymPlanner

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, 
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        // Initialize KMP modules
        // KoinKt.doInitKoin()
        return true
    }
}
```

## Contributing

See the main [README.md](../README.md) for contribution guidelines.

## License

See [LICENSE](../LICENSE) for details.

