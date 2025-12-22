#!/bin/bash

# Script to build XCFramework and generate Swift Package manifest
# Usage: ./build-xcframework.sh [version]

set -e

VERSION=${1:-"0.0.1-dev"}
FRAMEWORK_NAME="SharedGymPlanner"
BUILD_DIR="shared/build/XCFrameworks/release"
OUTPUT_DIR="$BUILD_DIR"

echo "🚀 Building XCFramework for $FRAMEWORK_NAME version $VERSION"

# Clean previous builds
echo "🧹 Cleaning previous builds..."
./gradlew clean

# Build XCFramework
echo "🔨 Building XCFramework..."
./gradlew :shared:assembleSharedGymPlannerXCFramework

# Verify XCFramework was created
if [ ! -d "$OUTPUT_DIR/$FRAMEWORK_NAME.xcframework" ]; then
    echo "❌ XCFramework not found at $OUTPUT_DIR/$FRAMEWORK_NAME.xcframework"
    exit 1
fi

echo "✅ XCFramework built successfully"

# Navigate to output directory
cd "$OUTPUT_DIR"

# Create zip archive
echo "📦 Creating zip archive..."
if [ -f "$FRAMEWORK_NAME.xcframework.zip" ]; then
    rm "$FRAMEWORK_NAME.xcframework.zip"
fi
zip -r "$FRAMEWORK_NAME.xcframework.zip" "$FRAMEWORK_NAME.xcframework"

# Calculate checksum
echo "🔐 Calculating checksum..."
CHECKSUM=$(swift package compute-checksum "$FRAMEWORK_NAME.xcframework.zip")
echo "Checksum: $CHECKSUM"

# Create Package.swift
echo "📝 Generating Package.swift..."
cat > Package.swift << EOF
// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "$FRAMEWORK_NAME",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "$FRAMEWORK_NAME",
            targets: ["$FRAMEWORK_NAME"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "$FRAMEWORK_NAME",
            url: "https://github.com/IanArb/GymPlanner/releases/download/v${VERSION}/$FRAMEWORK_NAME.xcframework.zip",
            checksum: "${CHECKSUM}"
        )
    ]
)
EOF

echo "✅ Package.swift generated"
echo ""
echo "📦 Build complete!"
echo ""
echo "📂 Output directory: $(pwd)"
echo "📁 Files created:"
echo "   - $FRAMEWORK_NAME.xcframework.zip"
echo "   - Package.swift"
echo ""
echo "📋 Next steps:"
echo "   1. Create a git tag: git tag v$VERSION"
echo "   2. Push the tag: git push origin v$VERSION"
echo "   3. Upload $FRAMEWORK_NAME.xcframework.zip to GitHub release"
echo "   4. Update root Package.swift with the new checksum"
echo ""
echo "Checksum for release: $CHECKSUM"

