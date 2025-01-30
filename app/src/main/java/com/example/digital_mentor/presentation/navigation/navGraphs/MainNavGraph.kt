package com.example.digital_mentor.presentation.navigation.navGraphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.core.utils.Routes
import com.example.digital_mentor.presentation.layout.MainAppLayout
import com.example.digital_mentor.presentation.view.DirectLinksScreen
import com.example.digital_mentor.presentation.view.HomeScreen
import com.example.digital_mentor.presentation.view.IlliteracyTestScreen
import com.example.digital_mentor.presentation.view.LearningGuidesScreen
import com.example.digital_mentor.presentation.view.LiveSupportScreen
import com.example.digital_mentor.presentation.view.TestResultScreen
import com.example.digital_mentor.presentation.view.UserProfileScreen
import com.example.digital_mentor.presentation.view.VideoTutorialsScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainNavGraph(
    navController: NavController,
) {
    navigation<AppRoutes.MainGraph>(
        startDestination = Routes.Home
    ) {
        composable<Routes.Home> {
            MainAppLayout(navController = navController) { modifier ->
                HomeScreen(navController = navController, modifier = modifier)
            }
        }

        composable<Routes.IlliterateTest> {
            IlliteracyTestScreen(onTestCompleted = {
                navController.navigate(Routes.TestResult) {
                    popUpTo<Routes.IlliterateTest> {
                        inclusive = true
                    }
                }
            })
        }

        composable<Routes.TestResult> {
            MainAppLayout(navController = navController) { modifier ->
                TestResultScreen(modifier = modifier, onNavToHome = {
                    navController.navigate(Routes.Home)
                }, onNavToVideoTutorials = {
                    navController.navigate(Routes.VideoTutorials)
                }, onNavToLearningGuides = {
                    navController.navigate(Routes.LearningGuides)
                }, onNavToLiveSupport = {
                    navController.navigate(Routes.LiveSupport)
                })
            }
        }

        composable<Routes.DirectLinks> {
            MainAppLayout(navController = navController) { modifier ->
                DirectLinksScreen(modifier, onNavigateToHome = {
                    navController.navigate(Routes.Home)
                })
            }
        }

        composable<Routes.UserProfile> {
            MainAppLayout(navController = navController) { modifier ->
                UserProfileScreen(
                    onNavBack = {
                        navController.navigate(Routes.Home)
                    },
                    modifier = modifier
                )
            }
        }

        composable<Routes.LiveSupport> {
            MainAppLayout(navController = navController) { modifier ->
                LiveSupportScreen(modifier = modifier, onReturnToMenu = {
                    navController.navigate(Routes.Home)
                })
            }
        }

        composable<Routes.LearningGuides> {
            MainAppLayout(navController = navController) { modifier ->
                LearningGuidesScreen(modifier = modifier)
            }
        }

        composable<Routes.VideoTutorials> {
            MainAppLayout(navController = navController) { modifier ->
                VideoTutorialsScreen(modifier = modifier)
            }
        }
    }
}