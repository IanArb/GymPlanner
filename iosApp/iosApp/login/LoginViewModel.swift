//
//  LoginViewModel.swift
//  iosApp
//
//  Created by Ian Arbuckle on 07/11/2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Combine
import SharedGymPlanner

class LoginViewModel : ObservableObject {
    
    @Published var state: LoginUiState = .idle
    
    private let authRepository: AuthenticationRepository
    private let dataStoreRepository: DataStoreRepository
    
    init(
        authRepository: AuthenticationRepository,
        dataStoreRepository: DataStoreRepository,
    ) {
        self.authRepository = authRepository
        self.dataStoreRepository = dataStoreRepository
    }
    
    func checkExistingUser() async {
        do {
            let isAlreadySignedIn = try await dataStoreRepository.getBooleanData(key: DataStoreRepositoryKt.REMEMBER_ME_KEY)
            
            if isAlreadySignedIn != nil {
                let token = try await dataStoreRepository.getStringData(key: DataStoreRepositoryKt.AUTH_TOKEN_KEY)
                
                if let token = token, !token.isEmpty {
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
        
        if isUsernameValid && isPasswordValid {
            let login = Login(username: username, password: password)
            
            do {
                _ = try await authRepository.login(login: login)
                await persistLogin(shouldRememberMe: shouldRemmeberMe)
                state = .success
            } catch {
                // Handle error
                state = .error
                print("Login failed: \(error)")
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
    
    func persistLogin(shouldRememberMe: Bool) async {
        do {
            try await dataStoreRepository.saveData(
                key: DataStoreRepositoryKt.REMEMBER_ME_KEY,
                value: shouldRememberMe
            )
        } catch {
            print("Failed to save remember me: \(error)")
        }
    }
}
