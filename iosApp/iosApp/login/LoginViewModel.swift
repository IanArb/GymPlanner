//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Ian Arbuckle on 07/11/2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Combine
import SharedGymPlanner

class LoginViewModel: ObservableObject {
    @Published var state: LoginUiState = .idle

    private let authRepository: AuthenticationRepository
    private let dataStoreRepository: DataStoreRepository

    init(
        authRepository: AuthenticationRepository,
        dataStoreRepository: DataStoreRepository
    ) {
        self.authRepository = authRepository
        self.dataStoreRepository = dataStoreRepository
    }

    func checkExistingUser() async {
        do {
            let isAlreadySignedIn = try await dataStoreRepository
                .getBooleanData(key: DataStoreRepositoryKt.REMEMBER_ME_KEY)

            if isAlreadySignedIn != nil {
                let token = try await dataStoreRepository
                    .getStringData(key: DataStoreRepositoryKt.AUTH_TOKEN_KEY)

                if let token, !token.isEmpty {
                    state = .signedIn
                }
            }
        } catch {
            print("No existing user found")
        }
    }

    @MainActor
    func handleLogin(username: String, password: String, shouldRemmeberMe: Bool) async {
        state = .loading

        let isUsernameValid = validateUsername(username)
        let isPasswordValid = validatePassword(password)

        if isUsernameValid, isPasswordValid {
            let login = Login(username: username, password: password)

            do {
                let result = try await authRepository.login(login: login)

                // On success the repository returns an ApiResultSuccess wrapping a
                // LoginResponse. We must persist its JWT token so the shared data
                // sources can attach it to subsequent authenticated requests.
                if let success = result as? ApiResultSuccess<AnyObject>,
                   let response = success.value as? LoginResponse {
                    await persistLogin(response: response, shouldRememberMe: shouldRemmeberMe)
                    state = .success
                } else {
                    print("Login failed: \(String(describing: result.exceptionOrNull()))")
                    state = .error
                }
            } catch {
                print("Login failed")
                state = .error
            }
        }
    }

    func validateUsername(_ value: String) -> Bool {
        if value.isEmpty {
            return false
        }
        return true
    }

    func validatePassword(_ value: String) -> Bool {
        if value.isEmpty {
            return false
        }
        return true
    }

    func persistLogin(response: LoginResponse, shouldRememberMe: Bool) async {
        do {
            // Persist the JWT token (and user id) so authenticated requests succeed.
            try await dataStoreRepository.saveData(
                key: DataStoreRepositoryKt.AUTH_TOKEN_KEY,
                value: response.token
            )
            try await dataStoreRepository.saveData(
                key: DataStoreRepositoryKt.USER_ID,
                value: response.userId
            )
            try await dataStoreRepository.saveData(
                key: DataStoreRepositoryKt.REMEMBER_ME_KEY,
                value: shouldRememberMe
            )
        } catch {
            print("Failed to persist login: \(error)")
        }
    }
}
