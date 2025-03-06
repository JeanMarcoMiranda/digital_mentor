package com.jacket.digital_mentor.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacket.digital_mentor.core.utils.AppRoutes
import com.jacket.digital_mentor.domain.usecase.auth.CheckSessionUseCase
import com.jacket.digital_mentor.presentation.intent.AppIntent
import com.jacket.digital_mentor.presentation.intent.AppState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class AppViewModel(
    private val checkSessionUseCase: CheckSessionUseCase,
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    AppIntent.Start -> checkSession()
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
        }
    }
}