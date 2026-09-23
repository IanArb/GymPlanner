//
//  FakeAuthenticationRepository.swift
//  iosAppTests
//
//  Test double for AuthenticationRepository.
//

import Foundation
import SharedGymPlanner

final class FakeAuthenticationRepository: AuthenticationRepository {
    /// The `ApiResult` returned by `login`.
    var loginResultToReturn: any ApiResult = ApiResultSuccess<AnyObject>(value: nil)

    /// When set, `login` throws this instead of returning.
    var errorToThrow: Error?

    /// Captures the `Login` values passed to `login`.
    private(set) var loginCalls: [Login] = []

    func __login(login: Login) async throws -> any ApiResult {
        loginCalls.append(login)
        if let errorToThrow {
            throw errorToThrow
        }
        return loginResultToReturn
    }

    func __register(register _: Register) async throws -> any ApiResult {
        ApiResultSuccess<AnyObject>(value: nil)
    }
}
