package com.example.digital_mentor.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.presentation.components.CustomTextField
import com.example.digital_mentor.presentation.intent.PasswordRecoveryIntent
import com.example.digital_mentor.presentation.intent.PasswordRecoveryState
import com.example.digital_mentor.presentation.intent.RegisterViewState
import com.example.digital_mentor.presentation.viewmodel.PasswordRecoveryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordRecoverySendEmailScreen(
    modifier: Modifier = Modifier,
    viewModel: PasswordRecoveryViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Título
        Text(
            text = "Envio de correo para cambio de contraseña",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de correo
        CustomTextField(
            value = (viewState as? PasswordRecoveryState.EmailVerify)?.email ?: "",
            keyboardType = KeyboardType.Email,
            onValueChange = { viewModel.sendIntent(PasswordRecoveryIntent.OnEmailChange(it)) },
            label = "Correo",
            placeholder = "Ingresa tu correo electrónico",
            error = (viewState as? PasswordRecoveryState.EmailVerify)?.emailError
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de enviar correo
        Button(
            onClick = {
                viewModel.sendIntent(
                    PasswordRecoveryIntent.SendRecoveryEmail
                )
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Enviar correo de recuperación",
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 7.dp)
            )
        }


        // Show toast messages based on the current state
        LaunchedEffect(viewState) {
            when (viewState) {
                is PasswordRecoveryState.Success -> {
                    val successMessage = (viewState as PasswordRecoveryState.Success).message
                    Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                }

                is PasswordRecoveryState.Error -> {
                    val errorMessage = (viewState as PasswordRecoveryState.Error).message
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }
}