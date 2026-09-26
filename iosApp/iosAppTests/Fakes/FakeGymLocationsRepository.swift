//
//  FakeGymLocationsRepository.swift
//  iosAppTests
//
//  Test double for GymLocationsRepository.
//

import Foundation
import SharedGymPlanner

final class FakeGymLocationsRepository: GymLocationsRepository {

    /// The `ApiResult` returned by `fetchGymLocations`.
    var resultToReturn: any ApiResult = ApiResultSuccess<AnyObject>(value: nil)

    /// When set, `fetchGymLocations` throws this instead of returning.
    var errorToThrow: Error?

    /// Number of times `fetchGymLocations` was called.
    private(set) var fetchCallCount = 0

    // SKIE exposes the suspend requirement with a `__` prefix; the public
    // `fetchGymLocations()` async method is a generated wrapper around it.
    func __fetchGymLocations() async throws -> Any? {
        fetchCallCount += 1
        if let errorToThrow {
            throw errorToThrow
        }
        return resultToReturn
    }
}
