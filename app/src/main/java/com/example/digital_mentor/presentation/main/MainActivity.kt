package com.example.digital_mentor.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.digital_mentor.presentation.login.LoginScreen
import com.example.digital_mentor.presentation.onboarding.OnboardingScreen
import com.example.digital_mentor.presentation.register.RegisterScreen
import com.example.digital_mentor.ui.theme.Digital_mentorTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Digital_mentorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
fun MainNavHost(navController: NavHostController) {
    NavHost(
        navController, startDestination = Route.Login
    ) {
        composable<Route.Onboarding> {
            OnboardingScreen(onStartClick = {
                navController.navigate(Route.Login)
            })
        }

        composable<Route.Login> {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(Route.Register)
                }
            )
        }
        composable<Route.Register> {
            RegisterScreen(onLoginClick = {
                navController.navigate(Route.Login)
            })
        }
    }
}

sealed interface Route {
    @Serializable
    data object Onboarding : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route
}
