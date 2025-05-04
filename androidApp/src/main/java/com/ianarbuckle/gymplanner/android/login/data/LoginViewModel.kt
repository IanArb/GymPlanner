package com.ianarbuckle.gymplanner.android.login.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ianarbuckle.gymplanner.authentication.AuthenticationRepository
import com.ianarbuckle.gymplanner.authentication.domain.Login
import com.ianarbuckle.gymplanner.storage.AUTH_TOKEN_KEY
import com.ianarbuckle.gymplanner.storage.DataStoreRepository
import com.ianarbuckle.gymplanner.storage.REMEMBER_ME_KEY
import com.ianarbuckle.gymplanner.storage.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    fun login(login: Login) {
        _loginState.update {
            LoginState.Loading
        }
        viewModelScope.launch {
            authenticationRepository.login(login).fold(
                onSuccess = { response ->
                    dataStoreRepository.saveData(key = USER_ID, value = response.userId)
                    dataStoreRepository.saveData(key = AUTH_TOKEN_KEY, value = response.token)
                    _loginState.update {
                        LoginState.Success(
                            response = response,
                        )
                    }
                },
                onFailure = {
                    _loginState.update {
                        LoginState.Error
                    }
                },
            )
        }
    }

    fun persistRememberMe(rememberMe: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.saveData(key = REMEMBER_ME_KEY, value = rememberMe)
        }
    }
}
