package com.example.digital_mentor.presentation.intent

sealed class PasswordRecoveryState {
    object Loading : PasswordRecoveryState()
    data class Success(val message: String) : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
    data class EmailVerify(
        val email: String,
        val emailError: String? = null
    ) : PasswordRecoveryState()

    data class UpdatePassword(
        val newPassword: String,
        val newPasswordError: String? = null,
        val newPasswordVisible: Boolean = false,
        val newPasswordVerify: String,
        val newPasswordVerifyError: String? = null,
        val newPasswordVerifyVisible: Boolean = false,
    ): PasswordRecoveryState()
}

sealed class PasswordRecoveryIntent {
    data class OnEmailChange(val email: String): PasswordRecoveryIntent()
    object SendRecoveryEmail : PasswordRecoveryIntent()
    data class OnNewPasswordChange(val newPassword: String): PasswordRecoveryIntent()
    data class onNewPasswordVerifyChange(val newPasswordVerify: String): PasswordRecoveryIntent()
    object UpdatePassword : PasswordRecoveryIntent()
    object ToggleNewPasswordVisibility : PasswordRecoveryIntent()
    object ToggleNewPasswordVerifyVisibility : PasswordRecoveryIntent()
    object SetEmailVerifyState: PasswordRecoveryIntent()
    object SetUpdatePasswordState: PasswordRecoveryIntent()
}

