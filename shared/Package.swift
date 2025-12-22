// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "SharedGymPlanner",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "SharedGymPlanner",
            targets: ["SharedGymPlanner"]
        )
    ],
    targets: [
        .binaryTarget(
            name: "SharedGymPlanner",
            // Replace with actual release URL and checksum when publishing
            // url: "https://github.com/IanArb/GymPlanner/releases/download/v1.0.0/SharedGymPlanner.xcframework.zip",
            // checksum: "YOUR_CHECKSUM_HERE"
            path: "./build/XCFrameworks/release/SharedGymPlanner.xcframework"
        )
    ]
)

