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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.6.0/SharedGymPlanner.xcframework.zip",
            checksum: "04354d9eb1c3974b09a24a403a630bdba5a715968846810c371fe07b578a3b12"
        )
    ]
)
