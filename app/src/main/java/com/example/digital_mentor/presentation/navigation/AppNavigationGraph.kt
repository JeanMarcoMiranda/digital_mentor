package com.example.digital_mentor.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.core.utils.Routes
import com.example.digital_mentor.presentation.intent.AppIntent
import com.example.digital_mentor.presentation.intent.AppState
import com.example.digital_mentor.presentation.navigation.navGraphs.authNavGraph
import com.example.digital_mentor.presentation.navigation.navGraphs.mainNavGraph
import com.example.digital_mentor.presentation.view.OnboardingScreen
import com.example.digital_mentor.presentation.viewmodel.AppViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigationGraph(
    viewModel: AppViewModel = koinViewModel(),
    startDestination: AppRoutes = AppRoutes.Onboarding
) {
    val navController = rememberNavController()
    val viewState by viewModel.viewState.collectAsState()

    // Get start route
    val startRoute = (viewState as? AppState.StartDestination)?.route ?: AppRoutes.AuthGraph
    val isTestPending = (viewState as? AppState.User)?.isTestPending ?: true

    LaunchedEffect(Unit) {
        viewModel.sendIntent(AppIntent.Start)
    }

    NavHost(
        navController = navController,
        startDestination
    ) {
        composable<AppRoutes.Onboarding> {
            OnboardingScreen(onStartClick = {
                navController.navigate(startRoute) {
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
            startDestination = if (isTestPending) Routes.IlliterateTest else Routes.Home
        )
    }
}
