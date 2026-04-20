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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.5.0/SharedGymPlanner.xcframework.zip",
            checksum: "5f5f04d4341e3c4af862e4eda90b3aa3dd795ed7f0f19831c5f32e8a14d59772"
        )
    ]
)
