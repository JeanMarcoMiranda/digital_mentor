package com.example.digital_mentor.presentation.intent

sealed class LoginIntent {
    data class UpdateEmail(val email: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    object Login : LoginIntent()
    object LoginWithGoogle : LoginIntent()
    object TogglePasswordVisibility : LoginIntent()
}

sealed class LoginViewState {
    object Idle : LoginViewState()
    object Loading : LoginViewState()
    data class Success(val message: String) : LoginViewState()
    data class Error(val error: String) : LoginViewState()
    data class Input(
        val email: String,
        val password: String,
        val passwordVisible: Boolean = false
    ) : LoginViewState()
}
