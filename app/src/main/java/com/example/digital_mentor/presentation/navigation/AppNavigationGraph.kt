package com.example.digital_mentor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.core.utils.Routes
import com.example.digital_mentor.presentation.navigation.navGraphs.authNavGraph
import com.example.digital_mentor.presentation.navigation.navGraphs.mainNavGraph
import com.example.digital_mentor.presentation.view.OnboardingScreen

@Composable
fun AppNavigationGraph(startDestination: AppRoutes = AppRoutes.Onboarding) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination
    ) {

        composable<AppRoutes.Onboarding> {
            OnboardingScreen(onStartClick = {
                navController.navigate(Routes.Login) {
                    popUpTo<AppRoutes.Onboarding> {
                        inclusive = true
                    }
                }
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
