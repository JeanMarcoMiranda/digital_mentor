package com.jacket.digital_mentor.presentation.intent

import com.jacket.digital_mentor.core.utils.Routes
import com.jacket.digital_mentor.data.model.UserProfileEntity

sealed class LoginIntent {
    data class UpdateEmail(val email: String) : LoginIntent()
    data class UpdatePassword(val password: String) : LoginIntent()
    object Login : LoginIntent()
    object LoginWithGoogle : LoginIntent()
    object TogglePasswordVisibility : LoginIntent()
    object LoadUserData : LoginIntent()
}

sealed class LoginViewState {
    object Idle : LoginViewState()
    object Loading : LoginViewState()
    data class Success(val message: String, val isPending: Boolean? = null) : LoginViewState()
    data class Error(val error: String) : LoginViewState()
    data class Input(
        val email: String,
        val password: String,
        val passwordVisible: Boolean = false
    ) : LoginViewState()

    data class User(
        val userProfile: UserProfileEntity? = null,
        val isTestPending: Boolean,
    ) : LoginViewState()
}
