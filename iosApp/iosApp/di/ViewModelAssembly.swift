//
//  ViewModelAssembly.swift
//  iosApp
//
//  Created by Ian Arbuckle on 24/12/2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Swinject
import SharedGymPlanner

class ViewModelAssembly: Assembly {
    func assemble(container: Container) {
        container.register(LoginViewModel.self) { (resolver) in
            LoginViewModel(
                authRepository: resolver.resolve(AuthenticationRepository.self)!,
                dataStoreRepository: resolver.resolve(DataStoreRepository.self)!,
            )
        }
    }
}
