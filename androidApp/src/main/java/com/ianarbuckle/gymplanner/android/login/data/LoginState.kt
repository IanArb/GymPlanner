package com.ianarbuckle.gymplanner.android.login.data

import androidx.compose.runtime.Stable
import com.ianarbuckle.gymplanner.authentication.domain.LoginResponse

sealed interface LoginState {
  data object Idle : LoginState

  data object Loading : LoginState

  data class Success(@Stable val response: LoginResponse) : LoginState

  data object Error : LoginState
}
