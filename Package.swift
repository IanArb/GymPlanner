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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.2.0/SharedGymPlanner.xcframework.zip",
            checksum: "ff83e588374f773acb7a802638ebecbc39a2a236ebd285102b848a5e9326a04b"
        )
    ]
)
