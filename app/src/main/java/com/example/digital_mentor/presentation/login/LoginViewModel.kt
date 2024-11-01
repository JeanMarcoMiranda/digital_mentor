package com.example.digital_mentor.presentation.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.SignInUseCase
import com.example.digital_mentor.domain.usecase.SignInWithGoogleUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginViewState>(LoginViewState.Input("", ""))
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private val intentChannel = Channel<LoginIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: LoginIntent, context: Context) {
        viewModelScope.launch {
            intentChannel.send(intent)

            if (intent is LoginIntent.LoginWithGoogle) {
                handleLoginWithGoogle(context)
            }
        }
    }

    private fun handleLoginWithGoogle(context: Context) {
        Log.d("LogWithGoogle", "Init")

        viewModelScope.launch {
            _viewState.value = LoginViewState.Loading
            val result = signInWithGoogleUseCase(context)
            _viewState.value = if (result.isSuccess) {
                Log.d("LogWithGoogle", "Success")
                LoginViewState.Success("Bienvenido con Google!")
            } else {
                Log.d("LogWithGoogle", "Error")
                LoginViewState.Error(
                    result.exceptionOrNull()?.message ?: "Error en inicio de sesión con Google"
                )
            }
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    is LoginIntent.UpdateEmail -> {
                        val currentState =
                            _viewState.value as? LoginViewState.Input ?: LoginViewState.Input(
                                "",
                                ""
                            )
                        _viewState.value = currentState.copy(email = intent.email)
                    }

                    is LoginIntent.UpdatePassword -> {
                        val currentState =
                            _viewState.value as? LoginViewState.Input ?: LoginViewState.Input(
                                "",
                                ""
                            )
                        _viewState.value = currentState.copy(password = intent.password)
                    }

                    LoginIntent.TogglePasswordVisibility -> {
                        val currentState =
                            _viewState.value as? LoginViewState.Input ?: LoginViewState.Input(
                                "",
                                ""
                            )
                        _viewState.value =
                            currentState.copy(passwordVisible = !currentState.passwordVisible)
                    }

                    LoginIntent.Login -> {
                        val currentState =
                            _viewState.value as? LoginViewState.Input ?: LoginViewState.Input(
                                "",
                                ""
                            )
                        if (currentState.email.isNotEmpty() && currentState.password.isNotEmpty()) {
                            _viewState.value = LoginViewState.Loading
                            viewModelScope.launch {
                                val result =
                                    signInUseCase(currentState.email, currentState.password)

                                if (result.isSuccess) {
                                    _viewState.value = LoginViewState.Success("Bienvenido con Supabase!")
                                } else {
                                    _viewState.value = LoginViewState.Error(
                                        result.exceptionOrNull()?.message
                                            ?: "Error en inicio de sesión con Supabase"
                                    )
                                }
                            }
                        }
                    }

                    LoginIntent.LoginWithGoogle -> {
                        // TODO: Implement some functionality
                    }
                }
            }
        }
    }
}