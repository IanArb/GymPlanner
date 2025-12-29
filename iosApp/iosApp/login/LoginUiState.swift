//
//  LoginUiState.swift
//  iosApp
//
//  Created by Ian Arbuckle on 28/12/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

enum LoginUiState: Equatable {
    case idle
    case loading
    case success
    case error
    case signedIn

    static func == (lhs: LoginUiState, rhs: LoginUiState) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle), (.loading, .loading), (.error, .error), (.success, .success):
            return true
        default:
            return false
        }
    }
}
