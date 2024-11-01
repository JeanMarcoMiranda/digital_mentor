package com.example.digital_mentor.presentation.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.digital_mentor.presentation.login.LoginViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onLoginClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()

    // Get passwordIsVisible current state
    val passwordVisible = (viewState as? RegisterViewState.Input)?.passwordVisible ?: false

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
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.name ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeName(it)) },
                label = "Nombre"
            )

            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.email ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeEmail(it)) },
                label = "Email"
            )

            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.password ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangePassword(it)) },
                label = "Contraseña",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordVisibilityChange = { viewModel.sendIntent(RegisterIntent.TogglePasswordVisibility) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = { viewModel.sendIntent(RegisterIntent.Register) },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse", fontSize = 14.sp, modifier = Modifier.padding(vertical = 7.dp))
            }

            DividerWithText("o")

            // Register with Google Button
            Button(
                onClick = { /* Acción de registro con Google aquí */ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
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
                    "Registrarse con Google",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 7.dp)
                )
            }
        }

        // Text to navigate back to Login
        Text(
            text = "Ya tienes una cuenta? Login",
            style = TextStyle(fontSize = 14.sp),
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clickable { onLoginClick() }
                .padding(bottom = 16.dp)
        )


        // Display loading indicator
        if (viewState is RegisterViewState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }

        // Show toast messages based on the current state
        LaunchedEffect(viewState) {
            when (viewState) {
                is RegisterViewState.Success -> {
                    val successMessage = (viewState as LoginViewState.Success).message
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                }

                is RegisterViewState.Error -> {
                    val errorMessage = (viewState as LoginViewState.Error).error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }
}

