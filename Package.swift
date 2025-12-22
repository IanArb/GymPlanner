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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.0.0/SharedGymPlanner.xcframework.zip",
            checksum: "3a7dacba0b0f24d3f589a15a6d751ddb930b79ad2d572ba402d01b2dbff7f81a"
        )
    ]
)
