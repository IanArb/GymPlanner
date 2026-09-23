//
//  FakeDataStoreRepository.swift
//  iosAppTests
//
//  Test double for DataStoreRepository. Stores writes in memory and returns
//  configured values for reads.
//

import Foundation
import SharedGymPlanner

final class FakeDataStoreRepository: DataStoreRepository {
    /// Value returned by `getBooleanData`.
    var booleanToReturn: KotlinBoolean?

    /// Value returned by `getStringData`.
    var stringToReturn: String?

    /// Captured `saveData` writes.
    private(set) var savedBooleans: [Bool] = []
    private(set) var savedStrings: [String] = []
    private(set) var didClear = false

    func __clearAllData() async throws {
        didClear = true
    }

    func __getBooleanData(key _: Datastore_preferences_corePreferencesKey<KotlinBoolean>) async throws -> KotlinBoolean? {
        booleanToReturn
    }

    func __getStringData(key _: Datastore_preferences_corePreferencesKey<NSString>) async throws -> String? {
        stringToReturn
    }

    func __saveData(key _: Datastore_preferences_corePreferencesKey<KotlinBoolean>, value: Bool) async throws {
        savedBooleans.append(value)
    }

    func __saveData(key _: Datastore_preferences_corePreferencesKey<NSString>, value: String) async throws {
        savedStrings.append(value)
    }
}
