package com.example.digital_mentor.presentation.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.domain.model.Topic
import com.example.digital_mentor.domain.model.TopicQuestion
import com.example.digital_mentor.presentation.intent.ChatMessage
import com.example.digital_mentor.presentation.intent.LiveSupportIntent
import com.example.digital_mentor.presentation.intent.LiveSupportState
import com.example.digital_mentor.presentation.viewmodel.LiveSupportViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiveSupportScreen(
    viewModel: LiveSupportViewModel = koinViewModel(),
    onReturnToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val state = viewState) {
            LiveSupportState.Start -> LiveSupportStartContent(
                onStartClicked = { viewModel.sendIntent(LiveSupportIntent.StartConsulting) }
            )

            is LiveSupportState.TopicSelection -> CommonContent(
                imageRes = R.drawable.man_image,
                text = "Por favor, selecciona un tema de interés:",
                content = {
                    TopicSelectionContent(
                        topics = state.topics,
                        onTopicSelected = { viewModel.sendIntent(LiveSupportIntent.SelectTopic(it)) }
                    )
                }
            )

            is LiveSupportState.QuestionConsult -> CommonContent(
                imageRes = R.drawable.man_image,
                text = "Estamos aquí para ayudarte. Responde a la siguiente pregunta:",
                content = {
                    ChatContent(
                        currentQuestion = state.currentQuestion,
                        messages = state.messages,
                        onAnswer = { optionId, answer ->
                            viewModel.sendIntent(
                                LiveSupportIntent.AnswerQuestion(
                                    optionId = optionId,
                                    answer = answer
                                )
                            )
                        },
                        typedAnswer = state.typedAnswer,
                        showTextField = state.showTextField,
                        onReturnToMenu = onReturnToMenu
                    )
                }
            )

            LiveSupportState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is LiveSupportState.Error -> Text(
                state.message,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun LiveSupportStartContent(
    onStartClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(
                40.dp
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Hola Juan! Mi nombre es Leonel. Será un gusto ayudarte.\n" +
                        "\n" +
                        "Por favor, dar click aqui",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onStartClicked,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                "Iniciar",
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.welcome_img),
            contentDescription = "Welcome",
            modifier = Modifier.size(300.dp),
            alignment = Alignment.CenterEnd
        )
    }
}

@Composable
fun CommonContent(
    imageRes: Int,
    text: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(vertical = 16.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}


@Composable
fun ChatContent(
    messages: List<ChatMessage>,
    currentQuestion: TopicQuestion,
    showTextField: Boolean,
    typedAnswer: String,
    onAnswer: (Int?, String) -> Unit,
    onReturnToMenu: () -> Unit
) {
    Log.d("LiveSupport", "These are the messages: $messages")
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Mostrar historial de mensajes
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(messages) { message ->
                ChatBubble(
                    text = message.message,
                    isSystem = message.sender.name == "System"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (currentQuestion.options.isNotEmpty() && !showTextField) {
            Column(modifier = Modifier.padding(16.dp)) {
                currentQuestion.options.forEach { option ->
                    Button(
                        onClick = { onAnswer(option.id, option.optionText) },
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        Text(option.optionText)
                    }
                }
            }
        } else {
            Button(
                onClick = onReturnToMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Regresar al menú principal")
            }
        }

        if (showTextField) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically // Asegura que ambos elementos estén centrados verticalmente
            ) {
                OutlinedTextField(
                    value = typedAnswer,
                    onValueChange = { newText ->
                        // Actualizar el texto del mensaje si fuera necesario
                    },
                    modifier = Modifier
                        .weight(1f) // Ocupa el espacio restante
                        .height(48.dp) // Reducir la altura del TextField
                        .padding(end = 8.dp), // Espaciado entre el TextField y el ícono
                    textStyle = TextStyle(fontSize = 14.sp), // Tamaño más pequeño
                    label = {
                        Text(
                            "Escribe tu respuesta",
                            fontSize = 12.sp
                        )
                    }, // Etiqueta más pequeña
                    singleLine = true, // Restringe a una sola línea
                    shape = RoundedCornerShape(8.dp) // Bordes más redondeados
                )
                IconButton(
                    onClick = { onAnswer(null, typedAnswer) },
                    enabled = typedAnswer.isNotBlank(), // Solo habilitar si hay texto
                    modifier = Modifier.size(48.dp) // Tamaño cuadrado consistente con la altura del TextField
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Send, // Ícono de enviar
                        contentDescription = "Enviar respuesta",
                        tint = if (typedAnswer.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    text: String,
    isSystem: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (isSystem) Alignment.CenterStart else Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (isSystem) Color.LightGray else Color(0xFFADD8E6))
                .padding(12.dp)
        ) {
            Text(
                text = text,
                color = if (isSystem) Color.Black else Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TopicSelectionContent(
    topics: List<Topic>,
    onTopicSelected: (Topic) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 15.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(topics) { topic ->
            Button(
                onClick = { onTopicSelected(topic) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(vertical = 4.dp)
            ) {
                Text(topic.name, textAlign = TextAlign.Center)
            }
        }
    }
}