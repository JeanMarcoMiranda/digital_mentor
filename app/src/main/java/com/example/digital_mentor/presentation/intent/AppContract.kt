package com.jacket.digital_mentor.presentation.intent

import com.jacket.digital_mentor.core.utils.AppRoutes
import com.jacket.digital_mentor.data.model.UserProfileEntity

sealed class AppIntent {
    object Start : AppIntent()
}

sealed class AppState {
    data class StartDestination(
        val route: AppRoutes = AppRoutes.AuthGraph
    ) : AppState()
}