package com.jacket.digital_mentor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacket.digital_mentor.domain.usecase.auth.GetCurrentUserInfoUseCase
import com.jacket.digital_mentor.domain.usecase.userProfile.GetUserProfileUseCase
import com.jacket.digital_mentor.presentation.intent.TestResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestResultViewModel(
    private val getUserInfo: GetCurrentUserInfoUseCase,
    private val getUserProfile: GetUserProfileUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<TestResultState>(
        TestResultState.Results(userProfile = null)
    )
    val viewState: StateFlow<TestResultState> = _viewState
        .onStart {
            loadUserProfile()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            TestResultState.Loading
        )


    private fun loadUserProfile() {
        viewModelScope.launch {
            val userInfoResult = getUserInfo()
            val currentState =
                _viewState.value as? TestResultState.Results ?: TestResultState.Results(
                    userProfile = null
                )

            _viewState.value = TestResultState.Loading

            if (userInfoResult.isSuccess) {
                val userInfo = userInfoResult.getOrNull()
                if (userInfo != null) {
                    val userProfileResult = getUserProfile(userInfo.id)

                    if (userProfileResult.isSuccess) {
                        val userProfile = userProfileResult.getOrNull()

                        if (userProfile != null) {
                            _viewState.value = currentState.copy(
                                userProfile = userProfile,
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
}