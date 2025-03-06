package com.jacket.digital_mentor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacket.digital_mentor.domain.usecase.auth.SendPasswordRecoveryEmailUseCase
import com.jacket.digital_mentor.domain.usecase.auth.UpdatePasswordUseCase
import com.jacket.digital_mentor.presentation.intent.PasswordRecoveryIntent
import com.jacket.digital_mentor.presentation.intent.PasswordRecoveryState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class PasswordRecoveryViewModel(
    private val sendPasswordRecoveryEmail: SendPasswordRecoveryEmailUseCase,
    private val updateUserPassword: UpdatePasswordUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<PasswordRecoveryState>(
        PasswordRecoveryState.EmailVerify(email = "")
    )
    val viewState: StateFlow<PasswordRecoveryState> = _viewState

    private val intentChannel = Channel<PasswordRecoveryIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: PasswordRecoveryIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    is PasswordRecoveryIntent.OnEmailChange -> {
                        val currentState = _viewState.value

                        if (currentState is PasswordRecoveryState.EmailVerify) {
                            _viewState.value = currentState.copy(email = intent.email)
                        }
                    }

                    PasswordRecoveryIntent.SendRecoveryEmail -> handleSendRecoveryEmail()
                    is PasswordRecoveryIntent.OnNewPasswordChange -> {
                        val currentState = _viewState.value
                        if (currentState is PasswordRecoveryState.UpdatePassword) {
                            _viewState.value = currentState.copy(newPassword = intent.newPassword)
                        }
                    }

                    is PasswordRecoveryIntent.onNewPasswordVerifyChange -> {
                        val currentState = _viewState.value
                        if (currentState is PasswordRecoveryState.UpdatePassword) {
                            _viewState.value =
                                currentState.copy(newPasswordVerify = intent.newPasswordVerify)
                        }
                    }

                    PasswordRecoveryIntent.UpdatePassword -> handleUpdatePassword()
                    is PasswordRecoveryIntent.SetEmailVerifyState -> {
                        _viewState.value = PasswordRecoveryState.EmailVerify(email = "")
                    }

                    is PasswordRecoveryIntent.SetUpdatePasswordState -> {
                        _viewState.value = PasswordRecoveryState.UpdatePassword(
                            newPassword = "",
                            newPasswordVerify = ""
                        )
                    }

                    PasswordRecoveryIntent.ToggleNewPasswordVerifyVisibility -> {
                        val currentState = _viewState.value as? PasswordRecoveryState.UpdatePassword
                            ?: PasswordRecoveryState.UpdatePassword(
                                newPassword = "",
                                newPasswordVerify = ""
                            )

                        _viewState.value =
                            currentState.copy(newPasswordVerifyVisible = !currentState.newPasswordVerifyVisible)
                    }

                    PasswordRecoveryIntent.ToggleNewPasswordVisibility -> {
                        val currentState = _viewState.value as? PasswordRecoveryState.UpdatePassword
                            ?: PasswordRecoveryState.UpdatePassword(
                                newPassword = "",
                                newPasswordVerify = ""
                            )

                        _viewState.value =
                            currentState.copy(newPasswordVisible = !currentState.newPasswordVisible)
                    }
                }
            }
        }
    }

    private fun handleSendRecoveryEmail() {
        viewModelScope.launch {
            val currentState = _viewState.value
            if (currentState is PasswordRecoveryState.EmailVerify) {
                val validatedState = validateInputSendEmail(currentState)

                if (
                    validatedState.emailError == null
                ) {
                    sendPasswordRecoveryEmail(currentState.email).onSuccess {
                        _viewState.value =
                            PasswordRecoveryState.Success("Se envio el correo de varificacion correctamente")
                    }.onFailure {
                        _viewState.value = PasswordRecoveryState.Error(
                            "No se pudo enviar el correo, vuelva a intentarlo"
                        )
                    }
                } else {
                    _viewState.value = validatedState
                }
            }
        }
    }

    private fun handleUpdatePassword() {
        viewModelScope.launch {
            val currentState = _viewState.value
            if (currentState is PasswordRecoveryState.UpdatePassword) {
                val validatedState = validateInputUpdatePassword(currentState)

                if (validatedState.newPasswordError == null && validatedState.newPasswordVerifyError == null) {
                    updateUserPassword(currentState.newPassword).onSuccess {
                        _viewState.value =
                            PasswordRecoveryState.Success("La contraseña se actualizó correctamente.")
                    }.onFailure {
                        _viewState.value =
                            PasswordRecoveryState.Error("No se pudo actualizar la contraseña. Inténtelo de nuevo.")
                    }
                } else {
                    _viewState.value = validatedState
                }
            }
        }
    }

    private fun validateInputSendEmail(input: PasswordRecoveryState.EmailVerify): PasswordRecoveryState.EmailVerify {
        val emailError = if (input.email.isEmpty()) "Debe llenar el correo electronico" else null

        return input.copy(
            emailError = emailError
        )
    }

    private fun validateInputUpdatePassword(input: PasswordRecoveryState.UpdatePassword): PasswordRecoveryState.UpdatePassword {
        val newPasswordError = when {
            input.newPassword.isEmpty() -> "Debe ingresar una nueva contraseña."
            input.newPassword.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
            else -> null
        }

        val newPasswordVerifyError = when {
            input.newPasswordVerify.isEmpty() -> "Debe confirmar la nueva contraseña."
            input.newPassword != input.newPasswordVerify -> "Las contraseñas no coinciden."
            else -> null
        }

        return input.copy(
            newPasswordError = newPasswordError,
            newPasswordVerifyError = newPasswordVerifyError
        )
    }
}