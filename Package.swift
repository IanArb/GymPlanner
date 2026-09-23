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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.11.0/SharedGymPlanner.xcframework.zip",
            checksum: "8bb2b8f91d72be6d24467caef9859a473532fcc5834610023ebc7fd96adc7b59"
        )
    ]
)
