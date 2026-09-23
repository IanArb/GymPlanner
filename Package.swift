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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.12.0/SharedGymPlanner.xcframework.zip",
            checksum: "87dda5d9d0c045f00ab295a6ca374cf8b7feb3f909b96fc7a1ddf1a72c1afd3f"
        )
    ]
)
