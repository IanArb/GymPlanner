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
            url: "https://github.com/IanArb/GymPlanner/releases/download/v1.9.0/SharedGymPlanner.xcframework.zip",
            checksum: "efea1219b335fc4a8b4cfd7d9fc849db25305d32e8662c9a20de7474a45eee96"
        )
    ]
)
