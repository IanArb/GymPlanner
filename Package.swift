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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.8.0/SharedGymPlanner.xcframework.zip",
            checksum: "a8d7333334c62c01f6cd3d011c53e9f47ecc2dd8a671cc8b87239267a8d8577e"
        )
    ]
)
