package com.ianarbuckle.gymplanner.android.login.presentation

import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse

sealed interface LoginState {
    data object Idle : LoginState
    data object Loading : LoginState
    data class Success(val response: LoginResponse) : LoginState
    data object Error : LoginState
}