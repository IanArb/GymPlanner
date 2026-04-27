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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.7.0/SharedGymPlanner.xcframework.zip",
            checksum: "4d93f6463bf9d2b8f841524881f9851d21df815f85c2712ee905d79ba3b1aa19"
        )
    ]
)
