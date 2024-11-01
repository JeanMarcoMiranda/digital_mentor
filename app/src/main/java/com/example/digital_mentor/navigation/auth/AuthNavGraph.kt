package com.example.digital_mentor.navigation.auth

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.digital_mentor.navigation.AppRoutes
import com.example.digital_mentor.navigation.Routes
import com.example.digital_mentor.presentation.login.LoginScreen
import com.example.digital_mentor.presentation.register.RegisterScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
) {
    navigation<AppRoutes.AuthGraph>(
        startDestination = Routes.Login,
    ) {
        composable<Routes.Login> {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(Routes.Register)
                },
                onLoginSuccess = {
                    navController.navigate(AppRoutes.MainGraph) {
                        popUpTo<AppRoutes.AuthGraph> {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable<Routes.Register> {
            RegisterScreen(onLoginClick = {
                navController.navigate(Routes.Login)
            })
        }
    }
}