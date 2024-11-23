package com.example.digital_mentor.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.digital_mentor.presentation.viewmodel.PasswordRecoveryViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PasswordRecoverySendEmailScreen(
    modifier: Modifier = Modifier,
    viewModel: PasswordRecoveryViewModel = koinViewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Buenas")
    }
}