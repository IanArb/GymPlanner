//
//  RepositoryAssembly.swift
//  iosApp
//
//  Created by Ian Arbuckle on 24/12/2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import Swinject
import SharedGymPlanner

class RepositoryAssembly: Assembly {
    func assemble(container: Container) {
        container.register(AuthenticationRepository.self) { _ in
            DefaultAuthenticationRepository()
        }.inObjectScope(.container)

        container.register(DataStoreRepository.self) { _ in
            DefaultDataStoreRepository()
        }.inObjectScope(.container)
    }
}
