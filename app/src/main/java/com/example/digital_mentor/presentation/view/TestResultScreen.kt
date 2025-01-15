package com.example.digital_mentor.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.data.model.UserProfileEntity
import com.example.digital_mentor.presentation.intent.TestResultState
import com.example.digital_mentor.presentation.viewmodel.TestResultViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TestResultScreen(
    viewModel: TestResultViewModel = koinViewModel(),
    onNavToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = viewState) {
            TestResultState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is TestResultState.Results -> {
                if (state.userProfile == null) {
                    Text("No se pudieron cargar los datos del usuario correctamente")
                } else {
                    ResultContent(userProfile = state.userProfile, onNavToHome)
                }
            }
        }
    }
}

@Composable
fun ResultContent(
    userProfile: UserProfileEntity,
    onNavToHome: () -> Unit
) {
    val totalScore = 20 // Puntaje máximo actualizado
    val cumulativeScore = userProfile.cumulativeScore ?: 0 // Asignar 0 si es null
    val progress = (cumulativeScore / totalScore.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()

    // Definir el color del progreso según el porcentaje
    val progressColor = when (percentage) {
        in 0..25 -> Color.Red
        in 26..50 -> Color(0xFFFFA500) // Naranja
        in 51..75 -> Color.Yellow
        else -> Color.Green
    }

    // Envolver el contenido en un Scrollable Column
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Text(
            text = "Progreso actual",
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        // Nivel
        Text(
            text = "Nivel: ${userProfile.illiteracyLevel}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
        )

        // Mensaje nivel
        val levelBasedMessage = when (percentage) {
            in 0..25 -> "Necesita asistencia en todas las áreas tecnológicas."
            in 26..50 -> "Tiene conocimientos básicos, pero necesita mejorar en aspectos clave para manejarse de manera independiente."
            in 51..75 -> "Maneja las habilidades básicas con soltura, pero necesita mejorar en competencias avanzadas y seguridad digital."
            else -> "Tiene buen dominio de las herramientas digitales, incluyendo algunas competencias avanzadas, pero puede perfeccionar habilidades complejas."
        }
        Text(
            text = levelBasedMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Barra de progreso circular
        Box(
            modifier = Modifier.size(150.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = progressColor,
                strokeWidth = 8.dp
            )
            Text(
                text = "$percentage%",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Felicitaciones
        Text(
            text = "!Felicitaciones ${userProfile.name}!",
            style = MaterialTheme.typography.titleMedium.copy(
                lineHeight = 40.sp
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 25.dp)
        )

        // Mensaje motivacional
        val motivationalMessage = when (userProfile.cumulativeScore) {
            in 0..5 -> "Tranquilo, te guiaremos para aumentar tus conocimientos."
            in 6..10 -> "Vas por buen camino, pero sigamos reforzando."
            in 11..15 -> "Tu nivel es bueno. Creemos que puedes llegar a un nivel superior"
            else -> "Tienes un buen dominio del mundo digital"
        }
        Text(
            text = motivationalMessage,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onNavToHome,
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .height(60.dp)
                .padding(start = 8.dp)
        ) {
            Text(
                "Ir al inicio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}