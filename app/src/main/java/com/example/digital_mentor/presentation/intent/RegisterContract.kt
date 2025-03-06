package com.jacket.digital_mentor.presentation.intent

sealed class RegisterViewState {
    object Idle : RegisterViewState()
    object Loading : RegisterViewState()
    data class Success(val message: String) : RegisterViewState()
    data class Error(val error: String) : RegisterViewState()
    data class Input(
        val name: String,
        val email: String,
        val phoneNumber: String,
        val password: String,
        val passwordVisible: Boolean = false,
        val nameError: String? = null,
        val phoneError: String? = null,
        val emailError: String? = null,
        val passwordError: String? = null
    ) : RegisterViewState()
}

sealed class RegisterIntent {
    data class ChangeName(val name: String) : RegisterIntent()
    data class ChangePhone(val phone: String) : RegisterIntent()
    data class ChangeEmail(val email: String) : RegisterIntent()
    data class ChangePassword(val password: String) : RegisterIntent()
    object Register : RegisterIntent()
    object RegisterWithGoogle: RegisterIntent()
    object TogglePasswordVisibility : RegisterIntent()
}
