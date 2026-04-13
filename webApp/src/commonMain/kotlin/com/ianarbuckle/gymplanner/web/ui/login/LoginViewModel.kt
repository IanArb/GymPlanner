package com.ianarbuckle.gymplanner.web.ui.login

import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.USER_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed class LoginUiState {
    data object Idle : LoginUiState()

    data object Loading : LoginUiState()

    data class Success(val token: String) : LoginUiState()

    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(private val scope: CoroutineScope) : KoinComponent {

    private val repository: AuthenticationRepository by inject()
    private val dataStoreRepository: DataStoreRepository by inject()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _isCheckingAuth = MutableStateFlow(true)
    val isCheckingAuth: StateFlow<Boolean> = _isCheckingAuth

    init {
        scope.launch {
            try {
                val token = dataStoreRepository.getStringData(AUTH_TOKEN_KEY)
                _isAuthenticated.value = !token.isNullOrBlank()
            } catch (e: Exception) {
                _isAuthenticated.value = false
            } finally {
                _isCheckingAuth.value = false
            }
        }
    }

    fun dispatchAction(action: LoginAction) {
        when (action) {
            is LoginAction.Login -> login(username = action.username, password = action.password)
            is LoginAction.Logout -> logout()
        }
    }

    private fun login(username: String, password: String) {
        scope.launch {
            _uiState.value = LoginUiState.Loading
            val result = repository.login(Login(username = username, password = password))
            result.fold(
                onSuccess = { response ->
                    try {
                        dataStoreRepository.saveData(key = USER_ID, value = response.userId)
                        dataStoreRepository.saveData(key = AUTH_TOKEN_KEY, value = response.token)
                        _uiState.value = LoginUiState.Success(token = response.token)
                        _isAuthenticated.value = true
                    } catch (e: Exception) {
                        _uiState.value =
                            LoginUiState.Error(message = "Failed to save authentication data")
                    }
                },
                onFailure = { error ->
                    _uiState.value =
                        LoginUiState.Error(message = error.message ?: "Authentication failed")
                },
            )
        }
    }

    private fun logout() {
        scope.launch {
            try {
                dataStoreRepository.clearAllData()
            } finally {
                _isAuthenticated.value = false
                _uiState.value = LoginUiState.Idle
            }
        }
    }
}
