package com.example.digital_mentor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.SignUpUseCase
import com.example.digital_mentor.presentation.intent.RegisterIntent
import com.example.digital_mentor.presentation.intent.RegisterViewState
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
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
                                "",
                                ""
                            )

                        if (currentState.name.isNotEmpty() && currentState.email.isNotEmpty() && currentState.password.isNotEmpty()) {
                            Log.d("RegisterResult", "Here register")
                            _viewState.value = RegisterViewState.Loading
                            viewModelScope.launch {
                                val result = signUpUseCase(
                                    name = currentState.name,
                                    email = currentState.email,
                                    password = currentState.password
                                )

                                Log.d("RegisterResult", "Here2 register")
                                _viewState.value = if (result.isSuccess) {
                                    Log.d("RegisterResult", "Here3 register")
                                    RegisterViewState.Success("Usuario registrado correctamente")
                                } else {
                                    Log.d("RegisterResult", "Here4 register")
                                    RegisterViewState.Error(
                                        result.exceptionOrNull()?.message
                                            ?: "Error al registrar usuario en Supabase vuelve a intentarlo"
                                    )
                                }

                                Log.d("RegisterResult", "El valor registrado es" + result)
                            }
                        }
                    }

                    RegisterIntent.RegisterWithGoogle -> {
                        // TODO: Implement google auth logic
                    }
                }
            }
        }
    }
}