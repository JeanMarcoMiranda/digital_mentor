package com.example.digital_mentor.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.domain.usecase.CheckSessionUseCase
import com.example.digital_mentor.domain.usecase.GetCurrentUserInfoUseCase
import com.example.digital_mentor.domain.usecase.GetUserProfileUseCase
import com.example.digital_mentor.presentation.intent.AppIntent
import com.example.digital_mentor.presentation.intent.AppState
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


class AppViewModel(
    private val checkSessionUseCase: CheckSessionUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val getUserInfo: GetCurrentUserInfoUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<AppState>(AppState.StartDestination())
    val viewState: StateFlow<AppState> = _viewState.asStateFlow()

    private val intentChannel = Channel<AppIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: AppIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    AppIntent.Start -> checkSession()
                    AppIntent.LoadUserProfile -> loadUserProfile()
                }
            }
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            val isAuthenticated = checkSessionUseCase()
            val destination = if (isAuthenticated) {
                AppRoutes.MainGraph
            } else {
                AppRoutes.AuthGraph
            }

            val currentState =
                _viewState.value as? AppState.StartDestination ?: AppState.StartDestination()

            _viewState.value = currentState.copy(route = destination)

            if (isAuthenticated) {
                sendIntent(AppIntent.LoadUserProfile)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadUserProfile() {
        viewModelScope.launch {
            val userInfoResult = getUserInfo()
            val currentState = _viewState.value as? AppState.User ?: AppState.User()

            if (userInfoResult.isSuccess) {
                Log.d("UserProfile", "Here")
                val userInfo = userInfoResult.getOrNull()
                if (userInfo != null) {
                    Log.d("UserProfile", "Here2")
                    val userProfileResult = getUserProfile(userInfo.id)

                    if (userProfileResult.isSuccess) {
                        Log.d("UserProfile", "Here3")
                        val userProfile = userProfileResult.getOrNull()

                        if (userProfile != null) {
                            Log.d("UserProfile", "Here4")

                            Log.d("UserProfile", "This is the userProfile: $userProfile")
                            _viewState.value = currentState.copy(
                                userProfile = userProfile,
                                isTestPending = isTestPending(
                                    userProfile.lastTestDate
                                )
                            )
                        }
                    }
                } else {
                    _viewState.value = currentState.copy(userProfile = null)
                }
            } else {
                _viewState.value = currentState.copy(userProfile = null)
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