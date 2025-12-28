//
//  LoginView.swift
//  iosApp
//
//  Created by Ian Arbuckle on 07/11/2025.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import Swinject

struct LoginView: View {
    @State private var username: String = ""
    @State private var password: String = ""
    @State private var rememberMe: Bool = true
    @State private var usernameError: String? = "Username is required"
    @State private var passwordError: String? = "Password is required"
    @State private var loginError: String? = "Error logging in. Please try again."
    @State private var attemptedLogin: Bool = false
    @State private var shouldNavigateToDashboard: Bool = false
    
    @StateObject var viewModel: LoginViewModel
    
    init(container: Container) {
        _viewModel = StateObject(wrappedValue:container.resolve(LoginViewModel.self)!)
    }
    
    var body: some View {
        NavigationView {
            Group {
                switch viewModel.state {
                case .idle:
                    LoginForm { username, password in
                        Task {
                            await viewModel.handleLogin(
                                username: username,
                                password: password,
                                shouldRemmeberMe: rememberMe
                            )
                        }
                    }
                case .error:
                    LoginForm(onLoginClick:  { username, password in
                        Task {
                            await viewModel.handleLogin(
                                username: username,
                                password: password,
                                shouldRemmeberMe: rememberMe
                            )
                        }
                    }, hasLoginFailed: true, showLoading: false)
                case .loading:
                    LoginForm(onLoginClick:  { _,_ in
                        
                    }, hasLoginFailed: false, showLoading: true)
                case .success:
                    NavigationLink(destination: DashboardView()) {
                        DashboardView()
                     }
                case .signedIn:
                    NavigationLink(destination: DashboardView()) {
                        DashboardView()
                    }
                }
            }
        }
    }

    private func LoginForm(onLoginClick: @escaping (String, String) -> Void, hasLoginFailed: Bool = false, showLoading: Bool = false) -> some View {
    VStack(alignment: .leading, spacing: 0) {
        // Title
        Text("Login")
            .font(.largeTitle)
            .fontWeight(.regular)
            .padding(.horizontal, 24)
            .padding(.top, 40)
        
        Spacer()
        
        VStack(alignment: .leading, spacing: 16) {
            // Username Field
            VStack(alignment: .leading, spacing: 8) {
                TextField("Username", text: $username)
                    .textFieldStyle(PlainTextFieldStyle())
                    .textInputAutocapitalization(.never)
                    .padding()
                    .background(Color.white)
                    .overlay(
                        RoundedRectangle(cornerRadius: 4)
                            .stroke(usernameError != nil && attemptedLogin ? Color.red.opacity(0.3) : Color.gray.opacity(0.3), lineWidth: 1)
                    )
                    .onChange(of: username) { newValue in
                        viewModel.validateUsername(newValue)
                    }
                
                if let error = usernameError, attemptedLogin {
                    Text(error)
                        .font(.footnote)
                        .foregroundColor(.red)
                }
            }
            
            VStack(alignment: .leading, spacing: 8) {
                SecureField("Password", text: $password)
                    .textFieldStyle(PlainTextFieldStyle())
                    .padding()
                    .background(Color.white)
                    .overlay(
                        RoundedRectangle(cornerRadius: 4)
                            .stroke(passwordError != nil && attemptedLogin ? Color.red.opacity(0.3) : Color.gray.opacity(0.3), lineWidth: 1)
                    )
                    .onChange(of: password) { newValue in
                        viewModel.validatePassword(newValue)
                    }
                
                if let error = passwordError, attemptedLogin {
                    Text(error)
                        .font(.footnote)
                        .foregroundColor(.red)
                }
            }
            
            if let error = loginError, hasLoginFailed {
                Text(error)
                    .font(.subheadline)
                    .foregroundColor(.red)
                    .frame(maxWidth: .infinity, alignment: .center)
                    .padding(.vertical, 8)
            }
            
            HStack(spacing: 12) {
                Button(action: {
                    rememberMe.toggle()
                }) {
                    Image(systemName: rememberMe ? "checkmark.square.fill" : "square")
                        .foregroundColor(rememberMe ? Color(red: 0.27, green: 0.35, blue: 0.49) : Color.gray)
                        .font(.title3)
                }
                
                Text("Remember Me")
                    .foregroundColor(.primary)
                    .font(.body)
            }
            .padding(.top, 8)
            
            let buttonText = showLoading ? "" : "Login"

            // Login Button
            Button(action: {
                attemptedLogin = true
                onLoginClick(username, password)
            }) {
                HStack {
                    if showLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: .white))
                    }
                    Text(buttonText)
                        .font(.headline)
                        .foregroundColor(.white)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(Color(red: 0.27, green: 0.35, blue: 0.49))
                        .cornerRadius(25)
                }
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color(red: 0.27, green: 0.35, blue: 0.49))
                .cornerRadius(25)
            }
            .disabled(showLoading)
        }
        .padding(.horizontal, 24)
        
        Spacer()
    }
    .frame(maxWidth: .infinity, maxHeight: .infinity)
    .background(Color(UIColor.systemGray6))
    .edgesIgnoringSafeArea(.all)
}
}

