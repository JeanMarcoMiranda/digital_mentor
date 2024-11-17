package com.example.digital_mentor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.data.model.UserProfileEntityUpdate
import com.example.digital_mentor.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.example.digital_mentor.domain.usecase.userProfile.GetUserProfileUseCase
import com.example.digital_mentor.domain.usecase.userProfile.UpdateUserProfileUseCase
import com.example.digital_mentor.presentation.intent.UserProfileIntent
import com.example.digital_mentor.presentation.intent.UserProfileState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val getUserInfo: GetCurrentUserInfoUseCase,
    private val getUserProfile: GetUserProfileUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<UserProfileState>(
        UserProfileState.UserData(userProfile = null, email = null)
    )
    val viewState: StateFlow<UserProfileState> = _viewState
        .onStart {
            loadUserProfile()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserProfileState.Loading
        )

    private val intentChannel = Channel<UserProfileIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: UserProfileIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    UserProfileIntent.OpenEditCard -> openEditCard()
                    UserProfileIntent.CloseEditCard -> closeEditCard()
                    is UserProfileIntent.UpdateCardDetails -> updateCardDetails(
                        intent.cardNumber,
                        intent.expirationDate,
                        intent.cvv
                    )

                    UserProfileIntent.SaveCardDetails -> saveCardDetails()
                }
            }
        }
    }

    private fun openEditCard() {
        val currentState = _viewState.value
        if (currentState is UserProfileState.UserData) {
            _viewState.value = currentState.copy(
                isEditSheetOpen = true
            )
        }
    }

    private fun closeEditCard() {
        val currentState = _viewState.value
        if (currentState is UserProfileState.UserData) {
            _viewState.value = currentState.copy(
                isEditSheetOpen = false
            )
        }
    }

    private fun updateCardDetails(cardNumber: String?, expirationDate: String?, cvv: String?) {
        val currentState = _viewState.value
        if (currentState is UserProfileState.UserData) {
            _viewState.value = currentState.copy(
                cardNumber = cardNumber,
                expirationDate = expirationDate,
                cvv = cvv
            )
        }
    }

    private fun saveCardDetails() {
        val currentState = _viewState.value
        if (currentState is UserProfileState.UserData) {
            // Aquí puedes guardar los datos en Supabase u otra lógica persistente.
            println("Card Saved: ${currentState.cardNumber}, ${currentState.expirationDate}, ${currentState.cvv}")
            viewModelScope.launch {
                val user = getUserInfo().getOrNull()

                if (user != null) {
                    val updateProfileData = UserProfileEntityUpdate(
                        card = currentState.cardNumber,
                        expirationDate = currentState.expirationDate,
                        cvv = currentState.cvv
                    )

                    val updateResult = updateUserProfile(user.id, updateProfileData)

                    updateResult.onSuccess {
                        _viewState.value = currentState.copy(
                            successMessage = "Datos de la tarjeta actualizados exitosamente."
                        )
                        closeEditCard()
                    }.onFailure {
                        _viewState.value = currentState.copy(
                            errorMessage = updateResult.exceptionOrNull()?.message
                                ?: "Error al actualizar los datos. Vuelva a intentarlo"
                        )
                        closeEditCard()
                    }
                }
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            val userInfoResult = getUserInfo()

            val currentState =
                _viewState.value as? UserProfileState.UserData ?: UserProfileState.UserData(
                    userProfile = null,
                    email = null
                )

            _viewState.value = UserProfileState.Loading

            if (userInfoResult.isSuccess) {
                val userInfo = userInfoResult.getOrNull()
                if (userInfo != null) {
                    val email = userInfo.email
                    val userProfileResult = getUserProfile(userInfo.id)

                    if (userProfileResult.isSuccess) {
                        val userProfile = userProfileResult.getOrNull()

                        if (userProfile != null) {
                            _viewState.value = currentState.copy(
                                userProfile = userProfile,
                                email = email
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

    fun clearMessage() {
        val currentState = _viewState.value
        if (currentState is UserProfileState.UserData) {
            _viewState.value = currentState.copy(successMessage = null, errorMessage = null)
        }
    }
}