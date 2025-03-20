package com.jacket.digital_mentor.presentation.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jacket.digital_mentor.data.model.UserProfileEntity
import com.jacket.digital_mentor.presentation.intent.LiveSupportIntent
import com.jacket.digital_mentor.presentation.intent.TestResultState
import com.jacket.digital_mentor.presentation.viewmodel.TestResultViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TestResultScreen(
    viewModel: TestResultViewModel = koinViewModel(),
    onNavToHome: () -> Unit,
    onNavToVideoTutorials: () -> Unit,
    onNavToLearningGuides: () -> Unit,
    onNavToLiveSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = viewState) {
            TestResultState.Loading -> {
                CircularProgressIndicator()
            }
            is TestResultState.Results -> {
                state.userProfile?.let { userProfile ->
                    ResultContent(
                        userProfile = userProfile,
                        onNavToHome,
                        onNavToVideoTutorials,
                        onNavToLearningGuides,
                        onNavToLiveSupport,
                        onAppTutorial = {
                            val youtubeUrl = "https://www.youtube.com/watch?v=liIK0G2Wfuw"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)).apply {
                                setPackage("com.google.android.youtube")
                            }
                            try {
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)))
                            }
                        }
                    )
                } ?: Text("No se pudieron cargar los datos del usuario correctamente")
            }
        }
    }
}

@Composable
fun ResultContent(
    userProfile: UserProfileEntity,
    onNavToHome: () -> Unit,
    onNavToVideoTutorials: () -> Unit,
    onNavToLearningGuides: () -> Unit,
    onNavToLiveSupport: () -> Unit,
    onAppTutorial: () -> Unit
) {
    val totalScore = 20
    val cumulativeScore = userProfile.cumulativeScore ?: 0
    val progress = (cumulativeScore / totalScore.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()

    val progressColor = when (percentage) {
        in 0..25 -> Color.Red
        in 26..50 -> Color(0xFFFFA500)
        in 51..75 -> Color.Yellow
        else -> Color.Green
    }

    val levelBasedMessage = when (percentage) {
        in 0..25 -> "Necesita asistencia en todas las áreas tecnológicas."
        in 26..50 -> "Tiene conocimientos básicos, pero necesita mejorar en aspectos clave."
        in 51..75 -> "Maneja las habilidades básicas, pero necesita mejorar en competencias avanzadas."
        else -> "Tiene buen dominio de las herramientas digitales."
    }

    val mainMessage = when (percentage) {
        in 0..25 -> "¡Te ayudo a mejorar, ${userProfile.name}!"
        in 26..50 -> "¡Sigamos mejorando, ${userProfile.name}!"
        in 51..75 -> "¡Vas bien, ${userProfile.name}!"
        else -> "¡Felicitaciones, ${userProfile.name}!"
    }

    val motivationalMessage = when (cumulativeScore) {
        in 0..5 -> "Tranquilo, te guiaremos para aumentar tus conocimientos."
        in 6..10 -> "Vas por buen camino, pero sigamos reforzando."
        in 11..15 -> "Tu nivel es bueno. Creemos que puedes llegar a un nivel superior."
        else -> "Tienes un buen dominio del mundo digital."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Progreso actual",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Nivel: ${userProfile.illiteracyLevel}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = levelBasedMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier.size(180.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                color = progressColor,
                strokeWidth = 12.dp,
                modifier = Modifier.size(160.dp)
            )
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .background(MaterialTheme.colorScheme.background, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = mainMessage,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = motivationalMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Ubicar los botones en la parte inferior de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val buttonModifier = Modifier
                    .weight(1f)
                    .height(60.dp)

                Button(
                    onClick = onNavToLiveSupport,
                    shape = RoundedCornerShape(15.dp),
                    modifier = buttonModifier
                ) {
                    Text(
                        text = "Chatear con un mentor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = onNavToVideoTutorials,
                    shape = RoundedCornerShape(15.dp),
                    modifier = buttonModifier
                ) {
                    Text(
                        text = "Videos Tutoriales",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = onNavToLearningGuides,
                    shape = RoundedCornerShape(15.dp),
                    modifier = buttonModifier
                ) {
                    Text(
                        text = "Guías de Aprendizaje",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (percentage in 0..25) {
                Button(
                    onClick = onAppTutorial,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(
                        text = "Tutorial de la App",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            Button(
                onClick = onNavToHome,
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Ir al Inicio",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}