//
//  LoginViewModelTests.swift
//  iosAppTests
//

import Foundation
@testable import iosApp
import SharedGymPlanner
import Testing

@MainActor
struct LoginViewModelTests {
    // MARK: - Helpers

    private func makeSUT() -> (LoginViewModel, FakeAuthenticationRepository, FakeDataStoreRepository) {
        let auth = FakeAuthenticationRepository()
        let dataStore = FakeDataStoreRepository()
        let sut = LoginViewModel(authRepository: auth, dataStoreRepository: dataStore)
        return (sut, auth, dataStore)
    }

    // MARK: - Validation

    @Test func validateUsernameRejectsEmptyAndAcceptsNonEmpty() {
        let (sut, _, _) = makeSUT()
        #expect(sut.validateUsername("") == false)
        #expect(sut.validateUsername("ian") == true)
    }

    @Test func validatePasswordRejectsEmptyAndAcceptsNonEmpty() {
        let (sut, _, _) = makeSUT()
        #expect(sut.validatePassword("") == false)
        #expect(sut.validatePassword("secret") == true)
    }

    // MARK: - handleLogin

    @Test func handleLoginSuccessSetsSuccessAndPersists() async {
        let (sut, auth, dataStore) = makeSUT()
        auth.loginResultToReturn = TestDataProvider.loginSuccess(
            TestDataProvider.loginResponse(userId: "user-1", token: "jwt-abc")
        )

        await sut.handleLogin(username: "ian", password: "secret", shouldRemmeberMe: true)

        #expect(sut.state == .success)
        #expect(auth.loginCalls.count == 1)
        #expect(auth.loginCalls.first?.username == "ian")
        // persistLogin should have written the JWT token, user id and remember-me flag.
        #expect(dataStore.savedStrings.contains("jwt-abc"))
        #expect(dataStore.savedStrings.contains("user-1"))
        #expect(dataStore.savedBooleans == [true])
    }

    @Test func handleLoginApiFailureSetsError() async {
        let (sut, auth, _) = makeSUT()
        auth.loginResultToReturn = TestDataProvider.failure(message: "invalid credentials")

        await sut.handleLogin(username: "ian", password: "wrong", shouldRemmeberMe: false)

        #expect(sut.state == .error)
    }

    @Test func handleLoginWithInvalidInputDoesNotCallRepository() async {
        let (sut, auth, _) = makeSUT()

        await sut.handleLogin(username: "", password: "", shouldRemmeberMe: false)

        #expect(auth.loginCalls.isEmpty)
        #expect(sut.state != .success)
    }

    // MARK: - checkExistingUser

    @Test func checkExistingUserSignsInWhenRememberedAndTokenPresent() async {
        let (sut, _, dataStore) = makeSUT()
        dataStore.booleanToReturn = KotlinBoolean(bool: true)
        dataStore.stringToReturn = "a-valid-token"

        await sut.checkExistingUser()

        if case .signedIn = sut.state { } else {
            Issue.record("Expected state to be .signedIn, got \(sut.state)")
        }
    }

    @Test func checkExistingUserStaysIdleWhenNotRemembered() async {
        let (sut, _, dataStore) = makeSUT()
        dataStore.booleanToReturn = nil

        await sut.checkExistingUser()

        #expect(sut.state == .idle)
    }

    @Test func checkExistingUserStaysIdleWhenTokenEmpty() async {
        let (sut, _, dataStore) = makeSUT()
        dataStore.booleanToReturn = KotlinBoolean(bool: true)
        dataStore.stringToReturn = ""

        await sut.checkExistingUser()

        #expect(sut.state == .idle)
    }
}
