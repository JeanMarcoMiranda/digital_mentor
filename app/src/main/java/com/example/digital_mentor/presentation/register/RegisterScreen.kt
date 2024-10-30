package com.example.digital_mentor.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.core.ui.components.CustomTextField
import com.example.digital_mentor.presentation.login.LoginIntent
import com.example.digital_mentor.presentation.login.LoginViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onLoginClick: () -> Unit = {}
) {
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

            // Nombre input
//            OutlinedTextField(
//                value = (viewState as? RegisterViewState.Input)?.name ?: "",
//                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeName(it)) },
//                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, lineHeight = 18.sp),
//                placeholder = {
//                    Text(
//                        "Nombre",
//                        style = TextStyle(fontSize = 14.sp, lineHeight = 18.sp)
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//                    .height(48.dp)
//            )


            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.name ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeName(it)) },
                label = "Nombre"
            )

            // Email input
//            OutlinedTextField(
//                value = (viewState as? RegisterViewState.Input)?.email ?: "",
//                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeEmail(it)) },
//                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, lineHeight = 18.sp),
//                placeholder = {
//                    Text(
//                        "Correo",
//                        style = TextStyle(fontSize = 14.sp, lineHeight = 18.sp)
//                    )
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//                    .height(48.dp)
//            )


            CustomTextField(
                value = (viewState as? RegisterViewState.Input)?.email ?: "",
                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangeEmail(it)) },
                label = "Email"
            )
            Spacer(modifier = Modifier.height(15.dp))

            // Password input
//            OutlinedTextField(
//                value = (viewState as? RegisterViewState.Input)?.password ?: "",
//                onValueChange = { viewModel.sendIntent(RegisterIntent.ChangePassword(it))},
//                textStyle = TextStyle(color = Color.Black, fontSize = 14.sp, lineHeight = 18.sp),
//                placeholder = {
//                    Text(
//                        "Contraseña",
//                        style = TextStyle(fontSize = 14.sp, lineHeight = 18.sp)
//                    )
//                },
//                trailingIcon = {
//                    Icon(
//                        imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
//                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
//                        modifier = Modifier
//                            .size(20.dp)
//                            .clickable { viewModel.sendIntent((RegisterIntent.TogglePasswordVisibility)) }
//                    )
//                },
//                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            )


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
                onClick = { /* Acción de registro aquí */ },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse", fontSize = 14.sp, modifier = Modifier.padding(vertical = 7.dp))
            }

            CenteredTextDivider("o")

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
