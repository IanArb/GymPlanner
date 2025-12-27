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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.1.0/SharedGymPlanner.xcframework.zip",
            checksum: "cec05b57f34197d2bdd8f8bceb25f8a8c0d974a8dcc98cfe67d2b7a9010f72c0"
        )
    ]
)
