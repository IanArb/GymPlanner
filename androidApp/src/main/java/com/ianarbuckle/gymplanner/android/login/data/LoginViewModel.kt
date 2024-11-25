package com.ianarbuckle.gymplanner.android.login.data

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.android.utils.CoroutinesDispatcherProvider
import com.ianarbuckle.gymplanner.api.GymPlanner
import com.ianarbuckle.gymplanner.authentication.domain.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val gymPlanner: GymPlanner,
    private val dispatchers: CoroutinesDispatcherProvider,
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _rememberMe = Channel<Boolean>()
    val rememberMe = _rememberMe.receiveAsFlow()

    fun login(login: Login) {
        viewModelScope.launch(dispatchers.io) {
            gymPlanner.login(login).fold(
                onSuccess = { response ->
                    gymPlanner.saveAuthToken(response.token)
                    gymPlanner.saveUserId(response.userId)
                    _loginState.update {
                        LoginState.Success(
                            response = response
                        )
                    }
                },
                onFailure = {
                    _loginState.update {
                        LoginState.Error
                    }
                }
            )
        }
    }

    fun persistRememberMe(rememberMe: Boolean) {
        viewModelScope.launch(dispatchers.io) {
            gymPlanner.saveRememberMe(rememberMe)
        }
    }

    fun fetchNavigationEntryPoint() {
        viewModelScope.launch(dispatchers.io) {
            _rememberMe.send(gymPlanner.fetchRememberMe())
        }
    }
}