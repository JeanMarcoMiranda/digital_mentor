package com.example.digital_mentor.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.presentation.components.CustomTextField
import com.example.digital_mentor.presentation.intent.RegisterIntent
import com.example.digital_mentor.presentation.intent.RegisterViewState
import com.example.digital_mentor.presentation.viewmodel.RegisterViewModel
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
                keyboardType = KeyboardType.Text,
                label = "Nombre"
            )


            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.name ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeName(it)) },
                keyboardType = KeyboardType.Number,
                label = "Tarjeta"
            )

            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.email ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeEmail(it)) },
                keyboardType = KeyboardType.Email,
                label = "Email"
            )

            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.password ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangePassword(it)) },
                label = "Contraseña",
                keyboardType = KeyboardType.Password,
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
                    val successMessage = (viewState as RegisterViewState.Success).message
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                }

                is RegisterViewState.Error -> {
                    val errorMessage = (viewState as RegisterViewState.Error).error
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }
}
