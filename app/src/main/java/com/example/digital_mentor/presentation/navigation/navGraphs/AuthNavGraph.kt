package com.example.digital_mentor.presentation.navigation.navGraphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.core.utils.Routes
import com.example.digital_mentor.presentation.view.LoginScreen
import com.example.digital_mentor.presentation.view.PasswordRecoverySendEmailScreen
import com.example.digital_mentor.presentation.view.RegisterScreen

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
                navController = navController,
            )
        }
        composable<Routes.Register> {
            RegisterScreen(onLoginClick = {
                navController.navigate(Routes.Login)
            }, onRegisterSuccess = {
                navController.navigate(Routes.IlliterateTest) {
                    popUpTo<AppRoutes.AuthGraph> {
                        inclusive = true
                    }
                }
            })
        }
        composable<Routes.PasswordRecoverySendEmail> {
            PasswordRecoverySendEmailScreen()
        }
    }
}