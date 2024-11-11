package com.example.digital_mentor.presentation.intent

import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.data.model.UserProfileEntity

sealed class AppIntent {
    object Start : AppIntent()
    object LoadUserProfile: AppIntent()
}

sealed class AppState {
    data class StartDestination(
        val route: AppRoutes = AppRoutes.AuthGraph
    ) : AppState()
    data class User(
        val userProfile: UserProfileEntity? = null,
        val isTestPending: Boolean = false
    ) : AppState()
}