package com.jacket.digital_mentor.presentation.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jacket.digital_mentor.R
import com.jacket.digital_mentor.core.utils.AppRoutes
import com.jacket.digital_mentor.core.utils.Routes
import com.jacket.digital_mentor.presentation.components.CustomTextField
import com.jacket.digital_mentor.presentation.components.DividerWithText
import com.jacket.digital_mentor.presentation.intent.AppIntent
import com.jacket.digital_mentor.presentation.intent.LoginIntent
import com.jacket.digital_mentor.presentation.intent.LoginViewState
import com.jacket.digital_mentor.presentation.viewmodel.AppViewModel
import com.jacket.digital_mentor.presentation.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onRegisterClick: () -> Unit = {},
    navController: NavController,
) {
    val context = LocalContext.current
    val appViewModel = koinViewModel<AppViewModel>()
    val viewState by viewModel.viewState.collectAsState()

    // Get passwordIsVisible current state
    val passwordVisible = (viewState as? LoginViewState.Input)?.passwordVisible ?: false

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomTextField(
                value = (viewState as? LoginViewState.Input)?.email ?: "",
                keyboardType = KeyboardType.Email,
                onValueChange = { viewModel.sendIntent(LoginIntent.UpdateEmail(it), context) },
                label = "Correo",
                placeholder = "Ingresa tu correo electronico"
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                value = (viewState as? LoginViewState.Input)?.password ?: "",
                keyboardType = KeyboardType.Password,
                onValueChange = { viewModel.sendIntent(LoginIntent.UpdatePassword(it), context) },
                label = "Contraseña",
                placeholder = "Ingresa tu contraseña",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = {
                    viewModel.sendIntent(
                        LoginIntent.TogglePasswordVisibility,
                        context
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = {
                    viewModel.sendIntent(LoginIntent.Login, context)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 7.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Forgot password
            Text(
                text = "¿Olvidaste la contraseña?",
                style = TextStyle(
                    fontSize = 15.sp
                ),
                color = Color.Blue,
                modifier = Modifier
                    .clickable {
                        navController.navigate(Routes.PasswordRecoverySendEmail)
                    }
            )

            DividerWithText("o")

            // Login with Google Button
            Button(
                onClick = {
                    viewModel.sendIntent(LoginIntent.LoginWithGoogle, context)
                    appViewModel.sendIntent(AppIntent.Start)
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_login_google),
                    contentDescription = null,
                    modifier = Modifier
                        .size(35.dp)
                        .padding(end = 15.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = "Iniciar Sesión con Google",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 7.dp)
                )
            }
        }

        // Not registered yer
        Text(
            text = "¿No tienes una cuenta? Regístrate",
            style = TextStyle(
                fontSize = 14.sp
            ),
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable { onRegisterClick() }
                .padding(bottom = 16.dp)
        )

        // Display loading indicator
        if (viewState is LoginViewState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }

        // Show toast messages based on the current state
        LaunchedEffect(viewState) {
            when (viewState) {
                is LoginViewState.Success -> {
                    val successMessage = (viewState as LoginViewState.Success).message


                    val isPending = (viewState as LoginViewState.Success).isPending

                    if (isPending != null) {
                        val mainStartDestination =
                            if (isPending) Routes.IlliterateTest else Routes.Home
                        navController.navigate(mainStartDestination) {
                            popUpTo(AppRoutes.AuthGraph) { inclusive = true }
                        }
                    }

                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                }

                is LoginViewState.Error -> {
                    val errorMessage = (viewState as LoginViewState.Error).error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
//                    Toast.makeText(context, "Credenciales invalidas, vuelva a intentarlo.", Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }
}