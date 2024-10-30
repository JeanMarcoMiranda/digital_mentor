package com.example.digital_mentor.presentation.login

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