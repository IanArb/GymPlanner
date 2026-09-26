//
//  FakeGymLocationsRepository.swift
//  iosAppTests
//
//  Test double for GymLocationsRepository.
//

import Foundation
import SharedGymPlanner

final class FakeGymLocationsRepository: GymLocationsRepository {
    var resultToReturn: any ApiResult = ApiResultSuccess<AnyObject>(value: nil)

    var errorToThrow: Error?

    private(set) var fetchCallCount = 0

    func __fetchGymLocations() async throws -> Any? {
        fetchCallCount += 1
        if let errorToThrow {
            throw errorToThrow
        }
        return resultToReturn
    }
}
