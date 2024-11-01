package com.example.digital_mentor.presentation.login

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.core.ui.components.CustomTextField
import com.example.digital_mentor.core.ui.components.DividerWithText
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onRegisterClick: () -> Unit = {},
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
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
                onValueChange = { viewModel.sendIntent(LoginIntent.UpdateEmail(it), context) },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                value = (viewState as? LoginViewState.Input)?.password ?: "",
                onValueChange = { viewModel.sendIntent(LoginIntent.UpdatePassword(it), context) },
                label = "Contraseña",
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
                onClick = { viewModel.sendIntent(LoginIntent.Login, context) },
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

            DividerWithText("o")

            // Login with Google Button
            Button(
                onClick = { viewModel.sendIntent(LoginIntent.LoginWithGoogle, context) },
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
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                }
                is LoginViewState.Error -> {
                    val errorMessage = (viewState as LoginViewState.Error).error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
                else -> Unit
            }
        }
    }

    // Look for every state and show a message based on the current change of state
//    when (viewState) {
//        is LoginViewState.Loading -> { /* Mostrar indicador de carga */
//        }
//
//        is LoginViewState.Error -> { /* Mostrar mensaje de error */
//        }
//
//        is LoginViewState.Success -> { /* Navegar o mostrar mensaje de éxito */
//        }
//
//        else -> Unit
//    }

}