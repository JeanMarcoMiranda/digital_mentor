package com.example.digital_mentor.presentation.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.example.digital_mentor.domain.usecase.userProfile.GetUserProfileUseCase
import com.example.digital_mentor.domain.usecase.auth.SignInUseCase
import com.example.digital_mentor.domain.usecase.auth.SignInWithGoogleUseCase
import com.example.digital_mentor.presentation.intent.LoginIntent
import com.example.digital_mentor.presentation.intent.LoginViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@RequiresApi(Build.VERSION_CODES.O)
class LoginViewModel(
    private val signInUseCase: SignInUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val getUserInfo: GetCurrentUserInfoUseCase,
    private val getUserProfile: GetUserProfileUseCase
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
            if (result.isSuccess) {
                Log.d("LogWithGoogle", "Success")
//                LoginViewState.Success("Bienvenido con Google!")
                loadUserProfile()
            } else {
                Log.d("LogWithGoogle", "Error")
                Log.d("LogWithGoogle", result.exceptionOrNull()?.message ?: "")
                _viewState.value = LoginViewState.Error(
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
                                    loadUserProfile()

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

                    LoginIntent.LoadUserData -> loadUserProfile()
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadUserProfile() {
        viewModelScope.launch {
            val userInfoResult = getUserInfo()
            val currentState = _viewState.value as? LoginViewState.User ?: LoginViewState.User(
                isTestPending = false
            )

            if (userInfoResult.isSuccess) {
                val userInfo = userInfoResult.getOrNull()
                if (userInfo != null) {
                    val userProfileResult = getUserProfile(userInfo.id)

                    if (userProfileResult.isSuccess) {
                        val userProfile = userProfileResult.getOrNull()

                        if (userProfile != null) {
                            val testPending = isTestPending(userProfile.lastTestDate)

                            _viewState.value = currentState.copy(
                                userProfile = userProfile,
                                isTestPending = testPending,
                            )

                            _viewState.value =
                                LoginViewState.Success(
                                    "Bienvenido con Supabase!",
                                    isPending = testPending
                                )
                        }
                    }
                } else {
                    _viewState.value = currentState.copy(userProfile = null, isTestPending = false)
                }
            } else {
                _viewState.value = currentState.copy(userProfile = null, isTestPending = false)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isTestPending(lastTestDate: String?): Boolean {
        if (lastTestDate == null) {
            return true
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // Formato con tiempo
        val lastDateTime = LocalDateTime.parse(lastTestDate, formatter)
        val lastDate = lastDateTime.toLocalDate() // Extrae solo la fecha sin tiempo
        val currentDate = LocalDate.now()

        // Calcula la diferencia en meses
        val monthsDifference = ChronoUnit.MONTHS.between(lastDate, currentDate)

        Log.d("UserProfile", "Months difference: $monthsDifference")

        return monthsDifference >= 1
    }
}