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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.14.0/SharedGymPlanner.xcframework.zip",
            checksum: "b9afe10544cee22f0b81841405611301ed36b54dfecfc248b6f19b1375901c6d"
        )
    ]
)
