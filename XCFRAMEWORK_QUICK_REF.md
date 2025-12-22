# XCFramework Quick Reference

## 🚀 Quick Start

### For iOS Developers

Add to your `Package.swift`:
```swift
dependencies: [
    .package(url: "https://github.com/IanArb/GymPlanner", from: "1.0.0")
]
```

Or in Xcode: File → Add Package Dependencies → Enter repository URL

### For Android/KMP Developers

Build XCFramework locally:
```bash
./build-xcframework.sh 1.0.0
```

## 📋 Common Commands

### Build XCFramework
```bash
# Using the build script (recommended)
./build-xcframework.sh 1.0.0

# Using Gradle directly
./gradlew :shared:assembleSharedGymPlannerXCFramework --no-configuration-cache
```

### Build Individual Frameworks
```bash
# Device framework
./gradlew :shared:linkReleaseFrameworkIosArm64 --no-configuration-cache

# Simulator framework  
./gradlew :shared:linkReleaseFrameworkIosSimulatorArm64 --no-configuration-cache
```

### Calculate Checksum
```bash
cd shared/build/XCFrameworks/release
swift package compute-checksum SharedGymPlanner.xcframework.zip
```

### Clean Build
```bash
./gradlew clean
rm -rf shared/build/XCFrameworks/
```

## 🏷️ Release Process

### Automated Release (Recommended)

1. **Commit with conventional format:**
   ```bash
   git commit -m "feat(ios): add new feature"
   git push origin main
   ```

2. **semantic-release automatically:**
   - Analyzes commits
   - Determines version (1.0.0 → 1.1.0)
   - Generates CHANGELOG
   - Builds XCFramework
   - Creates GitHub release

3. **iOS developers install:**
   ```swift
   .package(url: "https://github.com/IanArb/GymPlanner", from: "1.1.0")
   ```

### Manual Release (Legacy)

1. **Create tag:**
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. **GitHub Actions automatically:**
   - Builds XCFramework
   - Creates release
   - Attaches artifacts

3. **iOS developers install:**
   ```swift
   .package(url: "https://github.com/IanArb/GymPlanner", from: "1.0.0")
   ```

### Commit Message Format

```bash
feat(scope): new feature      # Minor: 0.1.0
fix(scope): bug fix           # Patch: 0.0.1
feat!: breaking change        # Major: 1.0.0
docs: documentation           # No release
```

## 📁 Important Files

| File | Purpose |
|------|---------|
| `.github/workflows/build-xcframework.yml` | Main build/release workflow |
| `.github/workflows/validate-xcframework.yml` | PR validation workflow |
| `build-xcframework.sh` | Local build script |
| `shared/Package.swift` | Swift Package manifest template |
| `shared/SWIFT_PACKAGE.md` | iOS integration guide |

## 🔧 Troubleshooting

### "xcodebuild failed with exit code 70"
Frameworks weren't built. Run:
```bash
./gradlew clean
./gradlew :shared:assembleSharedGymPlannerXCFramework --rerun-tasks --no-configuration-cache
```

### "Configuration cache error"
Add to `gradle.properties`:
```properties
org.gradle.configuration-cache=false
```

### "Checksum mismatch"
Rebuild and recalculate:
```bash
./build-xcframework.sh 1.0.0
```

## 📦 Output Locations

| Artifact | Location |
|----------|----------|
| Device framework | `shared/build/bin/iosArm64/releaseFramework/` |
| Simulator framework | `shared/build/bin/iosSimulatorArm64/releaseFramework/` |
| XCFramework | `shared/build/XCFrameworks/release/SharedGymPlanner.xcframework` |
| Zip archive | `shared/build/XCFrameworks/release/SharedGymPlanner.xcframework.zip` |
| Package manifest | `shared/build/XCFrameworks/release/Package.swift` |

## 🔗 Resources

- [Full Workflow Documentation](.github/XCFRAMEWORK_WORKFLOW.md)
- [Semantic Release Guide](.github/SEMANTIC_RELEASE_GUIDE.md)
- [Contributing Guide](CONTRIBUTING.md)
- [iOS Integration Guide](../shared/SWIFT_PACKAGE.md)
- [Kotlin MPP Docs](https://kotlinlang.org/docs/multiplatform-mobile-getting-started.html)
- [Swift Package Manager Guide](https://developer.apple.com/documentation/xcode/distributing-binary-frameworks-as-swift-packages)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Semantic Versioning](https://semver.org/)

