package com.example.digital_mentor.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.core.ui.components.CustomTextField
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit = {},
) {
    val viewModel = koinViewModel<LoginViewModel>()
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
                onValueChange = { viewModel.sendIntent(LoginIntent.UpdateEmail(it)) },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(15.dp))

            CustomTextField(
                value = (viewState as? LoginViewState.Input)?.password ?: "",
                onValueChange = { viewModel.sendIntent(LoginIntent.UpdatePassword(it)) },
                label = "Contraseña",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = { viewModel.sendIntent(LoginIntent.TogglePasswordVisibility) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Button
            Button(
                onClick = { viewModel.sendIntent(LoginIntent.Login) },
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

            CenteredTextDivider("o")

            // Login with Google Button
            Button(
                onClick = { viewModel.sendIntent(LoginIntent.LoginWithGoogle) },
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
    }

    // Look for every state and show a message based on the current change of state
    when (viewState) {
        is LoginViewState.Loading -> { /* Mostrar indicador de carga */
        }

        is LoginViewState.Error -> { /* Mostrar mensaje de error */
        }

        is LoginViewState.Success -> { /* Navegar o mostrar mensaje de éxito */
        }

        else -> Unit
    }
}

@Composable
fun CenteredTextDivider(text: String) {
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
        ) {
            Divider(modifier = Modifier.weight(1f))
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Divider(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}