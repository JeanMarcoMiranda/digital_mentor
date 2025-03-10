package com.jacket.digital_mentor.core.utils

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

    @Serializable
    data object IlliterateTest: Routes

    @Serializable
    data object TestResult: Routes

    @Serializable
    data object UserProfile: Routes

    @Serializable
    data object LiveSupport: Routes

    @Serializable
    data object LearningGuides: Routes

    @Serializable
    data object PasswordRecoverySendEmail: Routes

    @Serializable
    data object PasswordRecoveryNewPassword: Routes

    @Serializable
    data object DirectLinks: Routes

    @Serializable
    data object VideoTutorials: Routes
}
