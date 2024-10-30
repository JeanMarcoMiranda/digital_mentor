package com.example.digital_mentor.presentation.register

sealed class RegisterIntent {
    data class ChangeName(val name: String) : RegisterIntent()
    data class ChangeEmail(val email: String) : RegisterIntent()
    data class ChangePassword(val password: String) : RegisterIntent()
    object Register : RegisterIntent()
    object RegisterWithGoogle: RegisterIntent()
    object TogglePasswordVisibility : RegisterIntent()
}