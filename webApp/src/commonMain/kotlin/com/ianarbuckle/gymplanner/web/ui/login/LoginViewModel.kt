package com.ianarbuckle.gymplanner.web.ui.login

import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.DefaultAuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Idle : LoginUiState()

    data object Loading : LoginUiState()

    data class Success(val token: String) : LoginUiState()

    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(
    private val scope: CoroutineScope,
    private val repository: AuthenticationRepository = DefaultAuthenticationRepository(),
) {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        scope.launch {
            _uiState.value = LoginUiState.Loading
            val result = repository.login(Login(username = username, password = password))
            result.fold(
                onSuccess = { response ->
                    _uiState.value = LoginUiState.Success(token = response.token)
                },
                onFailure = { error ->
                    _uiState.value =
                        LoginUiState.Error(message = error.message ?: "Authentication failed")
                },
            )
        }
    }
}
