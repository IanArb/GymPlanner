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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.13.0/SharedGymPlanner.xcframework.zip",
            checksum: "d4c6d84694fa17da1539f3e878642d48ac91dc1b121c6c77dfa7fe6489164f11"
        )
    ]
)
