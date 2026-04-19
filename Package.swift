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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.4.0/SharedGymPlanner.xcframework.zip",
            checksum: "1bfbc89eb7f24c6b11b86eb0c1456b3fe270dadcd3c1d2a217ed3c5e773994eb"
        )
    ]
)
