package com.example.digital_mentor.presentation.layout

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.digital_mentor.core.utils.AppRoutes
import com.example.digital_mentor.presentation.components.CustomTopAppBar
import com.example.digital_mentor.presentation.components.DrawerContent
import com.example.digital_mentor.presentation.intent.AppIntent
import com.example.digital_mentor.presentation.intent.MainLayoutIntent
import com.example.digital_mentor.presentation.intent.MainLayoutState
import com.example.digital_mentor.presentation.viewmodel.AppViewModel
import com.example.digital_mentor.presentation.viewmodel.MainLayoutViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppLayout(
    viewModel: MainLayoutViewModel = koinViewModel(),
    navController: NavController, screen: @Composable (modifier: Modifier) -> Unit
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()
    val appViewModel = koinViewModel<AppViewModel>()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState()
    )
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                DrawerContent(onLogoutClick = { viewModel.sendIntent(MainLayoutIntent.LogoutClicked) })
            }
        }
    ) {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    onNavMenuClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { paddingValues ->
            screen(
                Modifier.padding(paddingValues)
            )


            // Show toast messages based on the current state
            LaunchedEffect(viewState) {
                when (viewState) {
                    is MainLayoutState.Success -> {
                        Log.d("Logout", "Success")
                        val successMessage = (viewState as MainLayoutState.Success).message
                        appViewModel.sendIntent(AppIntent.Start)
                        navController.navigate(AppRoutes.Onboarding) {
                            popUpTo<AppRoutes.MainGraph> {
                                inclusive = true
                            }
                        }
                        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    }

                    is MainLayoutState.Error -> {
                        Log.d("Logout", "Error")
                        val errorMessage = (viewState as MainLayoutState.Error).error
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}