package com.example.digital_mentor.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.SignUpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val signUpUseCase: SignUpUseCase) : ViewModel() {
    private val _viewState = MutableStateFlow<RegisterViewState>(RegisterViewState.Idle)
    val viewState: StateFlow<RegisterViewState> = _viewState.asStateFlow()

    private val intentChannel = Channel<RegisterIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: RegisterIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect { intent ->
                when (intent) {
                    is RegisterIntent.ChangeName -> {
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
                                "",
                                ""
                            )
                        _viewState.value = currentState.copy(name = intent.name)
                    }

                    is RegisterIntent.ChangeEmail -> {
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
                                "",
                                ""
                            )
                        _viewState.value = currentState.copy(email = intent.email)
                    }

                    is RegisterIntent.ChangePassword -> {
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
                                "",
                                ""
                            )
                        _viewState.value = currentState.copy(password = intent.password)
                    }

                    RegisterIntent.TogglePasswordVisibility -> {
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
                                "",
                                ""
                            )
                        _viewState.value =
                            currentState.copy(passwordVisible = !currentState.passwordVisible)
                    }

                    RegisterIntent.Register -> {
                        _viewState.value = RegisterViewState.Loading
                        // TODO: Implement auth logic
                        _viewState.value = RegisterViewState.Success("Bienvenido!")
                    }

                    RegisterIntent.RegisterWithGoogle -> {
                        // TODO: Implement google auth logic
                    }
                }
            }
        }
    }
}