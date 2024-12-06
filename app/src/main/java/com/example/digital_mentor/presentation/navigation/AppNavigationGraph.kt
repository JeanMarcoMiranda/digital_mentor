package com.example.digital_mentor.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.presentation.intent.AppIntent
import com.example.digital_mentor.presentation.intent.AppState
import com.example.digital_mentor.presentation.intent.MainLayoutState
import com.example.digital_mentor.presentation.navigation.navGraphs.authNavGraph
import com.example.digital_mentor.presentation.navigation.navGraphs.mainNavGraph
import com.example.digital_mentor.presentation.view.OnboardingScreen
import com.example.digital_mentor.presentation.viewmodel.AppViewModel
import com.example.digital_mentor.presentation.viewmodel.MainLayoutViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigationGraph(
    viewModel: AppViewModel = koinViewModel(),
    startDestination: AppRoutes = AppRoutes.Onboarding
) {
    val navController = rememberNavController()
    val viewState by viewModel.viewState.collectAsState()

    val mainLayoutViewModel = koinViewModel<MainLayoutViewModel>()
    val mainViewState by mainLayoutViewModel.viewState.collectAsState()

    // Get start route
    val startRoute = (viewState as? AppState.StartDestination)?.route ?: AppRoutes.AuthGraph

    // Unificar la lógica de lanzamiento de efectos
    LaunchedEffect(viewState, mainViewState) {
        when {
            mainViewState is MainLayoutState.Success -> {
                viewModel.sendIntent(AppIntent.Start)
            }

            viewState is AppState.StartDestination -> {
                viewModel.sendIntent(AppIntent.Start)
            }
        }
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
        )
    }
}
