package com.example.digital_mentor.presentation.login

sealed class LoginIntent {
    data class UpdateEmail(val email: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    object Login : LoginIntent()
    object LoginWithGoogle : LoginIntent()
    object NavigateToRegister : LoginIntent()
    object TogglePasswordVisibility : LoginIntent()
}