package com.example.digital_mentor.presentation.navigation.navGraphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.core.utils.Routes
import com.example.digital_mentor.presentation.layout.MainAppLayout
import com.example.digital_mentor.presentation.view.HomeScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
) {
    navigation<AppRoutes.MainGraph>(
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            MainAppLayout(navController = navController,) {
                HomeScreen()
            }
        }
    }
}