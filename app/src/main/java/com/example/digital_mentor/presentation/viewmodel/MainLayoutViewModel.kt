package com.example.digital_mentor.presentation.viewmodel

import androidx.compose.material3.DrawerValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.SignOutUseCase
import com.example.digital_mentor.presentation.intent.MainLayoutIntent
import com.example.digital_mentor.presentation.intent.MainLayoutState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainLayoutViewModel(
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainLayoutState>(MainLayoutState.Drawer())
    val viewState: StateFlow<MainLayoutState> = _viewState.asStateFlow()

    private val intentChannel = Channel<MainLayoutIntent> { Channel.UNLIMITED }

    init {
        handleIntent()
    }

    fun sendIntent(intent: MainLayoutIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    is MainLayoutIntent.ToggleDrawer -> {
                        val currentState =
                            _viewState.value as? MainLayoutState.Drawer ?: MainLayoutState.Drawer()


                        if (currentState.isOpen == DrawerValue.Open) {
                            _viewState.value = currentState.copy(isOpen = DrawerValue.Closed)
                        } else {
                            _viewState.value = currentState.copy(isOpen = DrawerValue.Open)
                        }
                    }

                    MainLayoutIntent.LogoutClicked -> {
                        viewModelScope.launch {
                            val result = signOutUseCase()

                            if (result.isSuccess) {
                                _viewState.value = MainLayoutState.Success("Sesion cerrada exitoasamente")
                            } else {
                                _viewState.value = MainLayoutState.Error(
                                    result.exceptionOrNull()?.message
                                        ?: "No se pudo cerrar la sesion"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}