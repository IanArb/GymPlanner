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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.10.0/SharedGymPlanner.xcframework.zip",
            checksum: "6dcd0733ccb81fcd8a1946c651905f9085957d1e4923805234b8aa8c3b23f919"
        )
    ]
)
