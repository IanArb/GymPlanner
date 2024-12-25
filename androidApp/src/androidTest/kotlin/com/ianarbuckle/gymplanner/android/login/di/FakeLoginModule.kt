package com.ianarbuckle.gymplanner.android.login.di

import com.ianarbuckle.gymplanner.android.login.fakes.FakeAuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [LoginModule::class]
)
class FakeLoginModule {

    @Provides
    fun providesAuthenticationRepository(): AuthenticationRepository {
        return FakeAuthenticationRepository()
    }

}