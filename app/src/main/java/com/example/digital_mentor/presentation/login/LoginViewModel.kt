package com.example.digital_mentor.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.SignInUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<LoginViewState>(LoginViewState.Input("", ""))
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private val intentChannel = Channel<LoginIntent> { Channel.UNLIMITED }

    init {
        Log.d("LoginViewModel", "SignInUseCase: $signInUseCase")
        handleIntents()
    }

    fun sendIntent(intent: LoginIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
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

                                Log.d("LoginResult", "El valor es: " + result);
                            }
                        }

                        _viewState.value = LoginViewState.Success("Bienvenido!")
                    }

                    LoginIntent.LoginWithGoogle -> {
                        // TODO: Implement google auth logic
                    }

                    LoginIntent.NavigateToRegister -> {
                        // TODO: Implement logic to navigate to register
                    }
                }
            }
        }
    }
}