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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.3.0/SharedGymPlanner.xcframework.zip",
            checksum: "cc512402016c6c09948fc0a87da6473fba8c4fcb71313e34f3ad2b634e2e6535"
        )
    ]
)
