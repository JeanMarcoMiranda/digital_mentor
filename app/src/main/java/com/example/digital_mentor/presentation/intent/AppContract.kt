package com.example.digital_mentor.presentation.intent

import com.example.digital_mentor.core.utils.AppRoutes

sealed class AppIntent {
    object Start : AppIntent()
}

sealed class AppState {
    data class StartDestination(
        val route: AppRoutes = AppRoutes.AuthGraph
    ) : AppState()
}