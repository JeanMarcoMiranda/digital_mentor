package com.example.digital_mentor.presentation.navigation.navGraphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.core.utils.Routes
import com.example.digital_mentor.presentation.layout.MainAppLayout
import com.example.digital_mentor.presentation.view.HomeScreen
import com.example.digital_mentor.presentation.view.IlliteracyTestScreen
import com.example.digital_mentor.presentation.view.TestResultScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
    startDestination: Routes
) {
    navigation<AppRoutes.MainGraph>(
        startDestination = startDestination
    ) {
        composable<Routes.Home> {
            MainAppLayout(navController = navController) {
                HomeScreen()
            }
        }

        composable<Routes.IlliterateTest> {
            MainAppLayout(navController = navController) {
                IlliteracyTestScreen()
            }
        }

        composable<Routes.TestResult> {
            MainAppLayout(navController = navController) {
                TestResultScreen()
            }
        }
    }
}