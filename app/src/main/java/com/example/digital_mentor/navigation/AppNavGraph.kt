package com.example.digital_mentor.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_mentor.navigation.auth.authNavGraph
import com.example.digital_mentor.navigation.main.mainNavGraph
import com.example.digital_mentor.presentation.onboarding.OnboardingScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavGraph(startDestination: AppRoutes = AppRoutes.Onboarding) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination
    ) {

        composable<AppRoutes.Onboarding> {
            OnboardingScreen(onStartClick = {
                navController.navigate(Routes.Login)
            })
        }
        authNavGraph(
            navController = navController,
        )
        mainNavGraph(
            navController = navController,
        )
    }
}