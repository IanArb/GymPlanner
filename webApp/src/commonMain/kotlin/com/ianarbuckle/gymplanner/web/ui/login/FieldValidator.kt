package com.ianarbuckle.gymplanner.web.ui.login

class FieldValidator {

    private val usernameRegex: Regex = Regex("^[a-zA-Z0-9][a-zA-Z0-9._-]{2,29}$")

    fun validateUsername(username: String): Boolean {
        return username.isNotBlank() && username.matches(usernameRegex)
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }
}
