package com.example.digital_mentor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.auth.SignUpUseCase
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
                                "",
                                ""
                            )
                        _viewState.value = currentState.copy(name = intent.name)
                    }

                    is RegisterIntent.ChangePhone -> {
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
                                "",
                                "",
                                ""
                            )

                        _viewState.value = currentState.copy(phoneNumber = intent.phone)
                    }

                    is RegisterIntent.ChangeEmail -> {
                        val currentState =
                            _viewState.value as? RegisterViewState.Input ?: RegisterViewState.Input(
                                "",
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
                                "",
                                ""
                            )
                        val validatedState = validateInput(currentState)

                        if (validatedState.nameError == null &&
                            validatedState.phoneError == null &&
                            validatedState.emailError == null &&
                            validatedState.passwordError == null
                        ) {
                            Log.d("RegisterResult", "Here register")
                            _viewState.value = RegisterViewState.Loading
                            viewModelScope.launch {
                                val result = signUpUseCase(
                                    name = currentState.name,
                                    phoneNumber = currentState.phoneNumber,
                                    email = currentState.email,
                                    password = currentState.password
                                )

                                _viewState.value = if (result.isSuccess) {
                                    RegisterViewState.Success("Usuario registrado correctamente")
                                } else {
                                    RegisterViewState.Error(
                                        result.exceptionOrNull()?.message
                                            ?: "Error al registrar usuario en Supabase vuelve a intentarlo"
                                    )
                                }
                            }
                        } else {
                            _viewState.value = validatedState
                        }
                    }

                    RegisterIntent.RegisterWithGoogle -> {
                        // TODO: Implement google auth logic
                    }
                }
            }
        }
    }

    private fun validateInput(input: RegisterViewState.Input): RegisterViewState.Input {
        val nameError = if (input.name.isBlank()) "Nombre es obligatorio" else null
//        val cardError = when {
//            input.card.isBlank() -> "Tarjeta es obligatoria"
//            input.card.length != 16 -> "La tarjeta debe tener 16 digitos"
//            else -> null
//        }
        val phoneError = if (input.phoneNumber.isBlank()) "El telefono es obligatorio" else null
        val emailError = if (!input.email.contains("@")) "Correo inválido" else null
        val passwordError = if (input.password.length < 6) "Contraseña muy corta" else null

        return input.copy(
            nameError = nameError,
            phoneError = phoneError,
            emailError = emailError,
            passwordError = passwordError
        )
    }
}