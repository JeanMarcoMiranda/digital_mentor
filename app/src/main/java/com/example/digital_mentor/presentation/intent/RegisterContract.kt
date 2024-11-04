package com.example.digital_mentor.presentation.intent

sealed class RegisterViewState {
    object Idle : RegisterViewState()
    object Loading : RegisterViewState()
    data class Success(val message: String) : RegisterViewState()
    data class Error(val error: String) : RegisterViewState()
    data class Input(
        val name: String,
        val email: String,
        val card: String,
        val password: String,
        val passwordVisible: Boolean = false
    ) : RegisterViewState()
}

sealed class RegisterIntent {
    data class ChangeName(val name: String) : RegisterIntent()
    data class ChangeCard(val card: String) : RegisterIntent()
    data class ChangeEmail(val email: String) : RegisterIntent()
    data class ChangePassword(val password: String) : RegisterIntent()
    object Register : RegisterIntent()
    object RegisterWithGoogle: RegisterIntent()
    object TogglePasswordVisibility : RegisterIntent()
}
