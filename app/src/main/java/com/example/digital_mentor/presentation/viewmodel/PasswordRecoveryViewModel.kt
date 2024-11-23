package com.example.digital_mentor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.auth.SendPasswordRecoveryEmailUseCase
import com.example.digital_mentor.domain.usecase.auth.UpdatePasswordUseCase
import com.example.digital_mentor.presentation.intent.PasswordRecoveryIntent
import com.example.digital_mentor.presentation.intent.PasswordRecoveryState
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
                    is PasswordRecoveryIntent.OnNewPasswordChange -> Unit
                    PasswordRecoveryIntent.UpdatePassword -> Unit
                    is PasswordRecoveryIntent.onNewPasswordVerifyChange -> Unit
                }
            }
        }
    }

    private fun handleSendRecoveryEmail() {
        viewModelScope.launch {
            val currentState = _viewState.value
            if (currentState is PasswordRecoveryState.EmailVerify) {
                val result = sendPasswordRecoveryEmail(currentState.email)
            }
        }
    }
}