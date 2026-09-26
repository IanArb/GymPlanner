//
//  TestDataProvider.swift
//  iosAppTests
//
//  Centralised test data for the view-model tests.
//

import Foundation
import SharedGymPlanner

enum TestDataProvider {
    // MARK: - Kotlin domain models

    static func kotlinFitnessClass(
        name: String = "Yoga Flow",
        description: String = "A gentle flow class.",
        imageUrl: String = "https://example.com/yoga.jpg",
        startTime: String = "16:00",
        endTime: String = "17:00",
        dayOfWeek: String = "MONDAY"
    ) -> SharedGymPlanner.FitnessClass {
        SharedGymPlanner.FitnessClass(
            dayOfWeek: dayOfWeek,
            description: description,
            duration: Duration(unit: "minutes", value: 60),
            endTime: endTime,
            imageUrl: imageUrl,
            name: name,
            startTime: startTime
        )
    }

    static let multipleClasses: [SharedGymPlanner.FitnessClass] = [
        kotlinFitnessClass(name: "Yoga Flow", startTime: "16:00", endTime: "17:00"),
        kotlinFitnessClass(name: "HIIT", startTime: "17:30", endTime: "18:15"),
        kotlinFitnessClass(name: "Spin", startTime: "19:00", endTime: "20:00"),
    ]

    // MARK: - ApiResult factories

    static func success(_ classes: [SharedGymPlanner.FitnessClass]) -> any ApiResult {
        ApiResultSuccess<AnyObject>(value: classes as AnyObject)
    }

    static func failure(message: String = "Something went wrong") -> any ApiResult {
        ApiResultFailure(error: KotlinThrowable(message: message))
    }

    // MARK: - Login

    static func loginResponse(
        userId: String = "user-1",
        token: String = "jwt-token",
        expiration: Int64 = 0
    ) -> LoginResponse {
        LoginResponse(userId: userId, token: token, expiration: expiration)
    }

    static func loginSuccess(_ response: LoginResponse = loginResponse()) -> any ApiResult {
        ApiResultSuccess<AnyObject>(value: response)
    }

    // MARK: - Gym locations

    static func kotlinGymLocation(
        title: String = "City Centre",
        subTitle: String = "Dublin 2",
        description: String = "Our flagship gym.",
        imageUrl: String = "https://example.com/gym.jpg"
    ) -> SharedGymPlanner.GymLocations {
        SharedGymPlanner.GymLocations(
            title: title,
            subTitle: subTitle,
            description: description,
            imageUrl: imageUrl
        )
    }

    static let multipleGymLocations: [SharedGymPlanner.GymLocations] = [
        kotlinGymLocation(title: "City Centre", subTitle: "Dublin 2"),
        kotlinGymLocation(title: "Northside", subTitle: "Dublin 9"),
        kotlinGymLocation(title: "Southside", subTitle: "Dublin 18"),
    ]

    static func gymLocationsSuccess(_ locations: [SharedGymPlanner.GymLocations]) -> any ApiResult {
        ApiResultSuccess<AnyObject>(value: locations as AnyObject)
    }

    // MARK: - Errors

    /// A bridgeable error for the "thrown" paths. Must be an `NSError` (not a
    /// plain Swift error) so it can cross the SKIE/ObjC async boundary safely.
    static var sampleError: NSError {
        NSError(domain: "iosAppTests", code: 1, userInfo: [NSLocalizedDescriptionKey: "sample error"])
    }
}
