package com.example.digital_mentor.core.utils

import kotlinx.serialization.Serializable

sealed interface AppRoutes {
    @Serializable
    data object Onboarding: AppRoutes

    @Serializable
    data object AuthGraph : AppRoutes

    @Serializable
    data object MainGraph : AppRoutes
}

sealed interface Routes {


    @Serializable
    data object Login: Routes

    @Serializable
    data object  Register: Routes

    @Serializable
    data object Home: Routes
}
