//
//  FakeFitnessClassRepository.swift
//  iosAppTests
//
//  Test double for FitnessClassRepository. Uses a fake (not a mock): responses
//  are configured up front and calls are captured for assertions.
//

import Foundation
import SharedGymPlanner

final class FakeFitnessClassRepository: FitnessClassRepository {
    /// The `ApiResult` returned by `fetchFitnessClasses`.
    var resultToReturn: any ApiResult = ApiResultSuccess<AnyObject>(value: nil)

    /// When set, `fetchFitnessClasses` throws this instead of returning.
    var errorToThrow: Error?

    /// Captures the `dayOfWeek` values passed to `fetchFitnessClasses`.
    private(set) var requestedDays: [String] = []

    func __fetchFitnessClasses(dayOfWeek: String) async throws -> any ApiResult {
        requestedDays.append(dayOfWeek)
        if let errorToThrow {
            throw errorToThrow
        }
        return resultToReturn
    }
}
