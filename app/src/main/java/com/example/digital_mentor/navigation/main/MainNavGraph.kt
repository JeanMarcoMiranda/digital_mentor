package com.example.digital_mentor.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.digital_mentor.navigation.AppRoutes
import com.example.digital_mentor.navigation.Routes
import com.example.digital_mentor.presentation.home.HomeScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
) {
    navigation<AppRoutes.MainGraph>(
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            HomeScreen()
        }
    }
}