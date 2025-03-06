package com.jacket.digital_mentor.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.jacket.digital_mentor.presentation.components.CustomTextField
import com.jacket.digital_mentor.presentation.intent.LoginViewState
import com.jacket.digital_mentor.presentation.intent.PasswordRecoveryIntent
import com.jacket.digital_mentor.presentation.intent.PasswordRecoveryState
import com.jacket.digital_mentor.presentation.viewmodel.PasswordRecoveryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordRecoveryNewPasswordFormScreen(
    viewModel: PasswordRecoveryViewModel = koinViewModel(),
    onPasswordUpdated: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.sendIntent(PasswordRecoveryIntent.SetUpdatePasswordState)
    }

    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()

    // Get passwordIsVisible current state
    val newPasswordVisible =
        (viewState as? PasswordRecoveryState.UpdatePassword)?.newPasswordVisible ?: false
    val newPasswordVerifyVisible =
        (viewState as? PasswordRecoveryState.UpdatePassword)?.newPasswordVerifyVisible ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Título
        Text(
            text = "Cambio de contraseña",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // New password field
        CustomTextField(
            value = (viewState as? PasswordRecoveryState.UpdatePassword)?.newPassword ?: "",
            keyboardType = KeyboardType.Password,
            onValueChange = { viewModel.sendIntent(PasswordRecoveryIntent.OnNewPasswordChange(it)) },
            label = "Nueva contraseña",
            placeholder = "Ingresa tu nueva contraseña",
            error = (viewState as? PasswordRecoveryState.UpdatePassword)?.newPasswordError, // Mostrar mensaje de error si existe
            isPassword = true,
            isPasswordVisible = newPasswordVisible,
            onPasswordVisibilityChange = {
                viewModel.sendIntent(
                    PasswordRecoveryIntent.ToggleNewPasswordVisibility,
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm new password field
        CustomTextField(
            value = (viewState as? PasswordRecoveryState.UpdatePassword)?.newPasswordVerify ?: "",
            keyboardType = KeyboardType.Password,
            onValueChange = {
                viewModel.sendIntent(
                    PasswordRecoveryIntent.onNewPasswordVerifyChange(
                        it
                    )
                )
            },
            label = "Confirmar contraseña",
            placeholder = "Confirma tu nueva contraseña",
            error = (viewState as? PasswordRecoveryState.UpdatePassword)?.newPasswordVerifyError, // Mostrar mensaje de error si existe
            isPassword = true,
            isPasswordVisible = newPasswordVerifyVisible,
            onPasswordVisibilityChange = {
                viewModel.sendIntent(
                    PasswordRecoveryIntent.ToggleNewPasswordVerifyVisibility,
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Update password button
        Button(
            onClick = { viewModel.sendIntent(PasswordRecoveryIntent.UpdatePassword) },
        ) {
            Text("Actualizar contraseña")
        }
    }

    // Manejo de estados
    when (viewState) {
        is PasswordRecoveryState.Loading -> {
            // Mostrar un indicador de carga
            CircularProgressIndicator()
        }

        is PasswordRecoveryState.Success -> {
            // Llamar al callback cuando la contraseña se actualice con éxito
            LaunchedEffect(Unit) {
                val errorMessage = (viewState as PasswordRecoveryState.Success).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                onPasswordUpdated()
            }
        }

        is PasswordRecoveryState.Error -> {
            val errorMessage = (viewState as PasswordRecoveryState.Error).message
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        else -> Unit // Otros estados no requieren acción aquí
    }
}