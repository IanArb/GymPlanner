package com.ianarbuckle.gymplanner.web.ui.login

sealed class LoginAction {
    data class Login(val username: String, val password: String) : LoginAction()

    data object Logout : LoginAction()
}
