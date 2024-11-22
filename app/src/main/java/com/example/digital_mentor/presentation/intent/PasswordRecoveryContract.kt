package com.example.digital_mentor.presentation.intent

sealed class PasswordRecoveryState {
    object Loading : PasswordRecoveryState()
    data class Success(val message: String) : PasswordRecoveryState()
    data class Error(val message: String) : PasswordRecoveryState()
    data class EmailVerify(
        val email: String
    ) : PasswordRecoveryState()

    data class UpdatePassword(
        val newPassword: String,
        val newPasswordVerify: String
    )
}

sealed class PasswordRecoveryIntent {
    data class OnEmailChange(val email: String): PasswordRecoveryIntent()
    object SendRecoveryEmail : PasswordRecoveryIntent()
    data class OnNewPasswordChange(val newPassword: String): PasswordRecoveryIntent()
    data class onNewPasswordVerifyChange(val newPasswordVerify: String): PasswordRecoveryIntent()
    object UpdatePassword : PasswordRecoveryIntent()
}

