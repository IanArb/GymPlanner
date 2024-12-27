package com.ianarbuckle.gymplanner.android.login.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.DefaultAuthenticationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class LoginModule {

    @Provides
    fun providesAuthenticationRepository(): AuthenticationRepository {
        return DefaultAuthenticationRepository()
    }
}