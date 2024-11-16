package com.example.digital_mentor.presentation.intent

import com.example.digital_mentor.data.model.UserProfileEntity


sealed class TestResultState {
    object Loading: TestResultState()

    data class Results(
        val userProfile: UserProfileEntity?
    ): TestResultState()
}