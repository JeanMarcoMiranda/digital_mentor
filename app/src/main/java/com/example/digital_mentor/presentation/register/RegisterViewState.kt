package com.example.digital_mentor.presentation.register


sealed class RegisterViewState {
    object Idle : RegisterViewState()
    object Loading : RegisterViewState()
    data class Success(val message: String) : RegisterViewState()
    data class Error(val error: String) : RegisterViewState()
    data class Input(
        val name: String,
        val email: String,
        val password: String,
        val passwordVisible: Boolean = false
    ) : RegisterViewState()
}