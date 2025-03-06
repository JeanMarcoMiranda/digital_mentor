package com.jacket.digital_mentor.presentation.navigation.navGraphs

import android.content.Intent
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.jacket.digital_mentor.core.utils.AppRoutes
import com.jacket.digital_mentor.core.utils.Routes
import com.jacket.digital_mentor.presentation.view.LoginScreen
import com.jacket.digital_mentor.presentation.view.PasswordRecoveryNewPasswordFormScreen
import com.jacket.digital_mentor.presentation.view.PasswordRecoverySendEmailScreen
import com.jacket.digital_mentor.presentation.view.RegisterScreen

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

        composable<Routes.PasswordRecoveryNewPassword>(deepLinks = listOf(
            navDeepLink {
                uriPattern = "digitalmentor://resetpassword"
                action = Intent.ACTION_VIEW
            }
        )) {
            PasswordRecoveryNewPasswordFormScreen(
                onPasswordUpdated = {
                    navController.navigate(Routes.Login) {
                        popUpTo<Routes.PasswordRecoveryNewPassword> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}