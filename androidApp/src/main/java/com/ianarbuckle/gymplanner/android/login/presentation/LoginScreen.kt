package com.ianarbuckle.gymplanner.android.login.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ianarbuckle.gymplanner.android.login.data.LoginState
import com.ianarbuckle.gymplanner.android.login.data.LoginViewModel
import com.ianarbuckle.gymplanner.authentication.domain.Login

@Composable
fun LoginScreen(
  contentPadding: PaddingValues,
  onNavigateTo: () -> Unit,
  modifier: Modifier = Modifier,
  loginViewModel: LoginViewModel = hiltViewModel(),
) {
  var username by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }
  var isUsernameValid by rememberSaveable { mutableStateOf(true) }
  var isPasswordValid by rememberSaveable { mutableStateOf(true) }
  var rememberMe by rememberSaveable { mutableStateOf(true) }

  val navigationEvent by rememberUpdatedState(onNavigateTo)

  val state = loginViewModel.loginState.collectAsState()

  when (state.value) {
    is LoginState.Success -> {
      loginViewModel.persistRememberMe(rememberMe)
      LaunchedEffect(Unit) { navigationEvent() }
    }

    is LoginState.Error -> {
      LoginScreenContent(
        innerPaddingValues = contentPadding,
        username = username,
        onUsernameChange = { username = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginClick = {
          if (username.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.login(Login(username, password))
          } else {
            isUsernameValid = username.isNotEmpty()
            isPasswordValid = password.isNotEmpty()
          }
        },
        isUsernameValid = isUsernameValid,
        isPasswordValid = isPasswordValid,
        rememberMe = rememberMe,
        onRememberMeChange = { rememberMe = it },
        isLoading = false,
        showError = true,
        modifier = modifier,
      )
    }

    LoginState.Idle -> {
      LoginScreenContent(
        innerPaddingValues = contentPadding,
        username = username,
        onUsernameChange = { username = it },
        password = password,
        onPasswordChange = { password = it },
        onLoginClick = {
          if (username.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.login(Login(username, password))
          } else {
            isUsernameValid = username.isNotEmpty()
            isPasswordValid = password.isNotEmpty()
          }
        },
        isUsernameValid = isUsernameValid,
        isPasswordValid = isPasswordValid,
        rememberMe = rememberMe,
        onRememberMeChange = { rememberMe = it },
        isLoading = false,
        modifier = modifier,
      )
    }
    LoginState.Loading -> {
      LoginScreenContent(
        innerPaddingValues = contentPadding,
        username = username,
        onUsernameChange = { username = it },
        password = password,
        onPasswordChange = { password = it },
        rememberMe = rememberMe,
        onRememberMeChange = { rememberMe = it },
        isLoading = true,
        modifier = modifier,
      )
    }
  }
}
