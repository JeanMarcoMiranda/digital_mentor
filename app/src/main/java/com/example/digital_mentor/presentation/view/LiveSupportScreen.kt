package com.example.digital_mentor.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digital_mentor.R
import com.example.digital_mentor.presentation.intent.ChatMessage
import com.example.digital_mentor.presentation.intent.ChatSender
import com.example.digital_mentor.presentation.intent.LiveSupportIntent
import com.example.digital_mentor.presentation.intent.LiveSupportState
import com.example.digital_mentor.presentation.viewmodel.LiveSupportViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LiveSupportScreen(
    viewModel: LiveSupportViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.viewState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val state = viewState) {
            LiveSupportState.Start -> LiveSupportStartContent(
                onStartClicked = { viewModel.sendIntent(LiveSupportIntent.StartConsulting) }
            )
            is LiveSupportState.ConsultChat -> {
                Text(
                    "Soporte en Vivo",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 40.dp)
                )
                LiveSupportConsultChat()
            }
            is LiveSupportState.Error -> Text("Error")
            LiveSupportState.Loading -> {
                // Here we show a load screen
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            is LiveSupportState.Success -> Text("Success")
            else -> Unit
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
                text = "Buenas",
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
fun LiveSupportConsultChat() {
    Box {
        // Test Image
        Image(
            painter = painterResource(id = R.drawable.man_image),
            contentDescription = "algo",
            modifier = Modifier.size(120.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "¿Cómo podemos ayudarte hoy?",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Elige una opcion",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }

        // Here the chat goes

    }
}

@Composable
fun TopicSelection(
    topics: List<String>,
    onTopicSelected: (String) -> Unit
) {
    Column {
        Text("Selecciona un tópico:", fontWeight = FontWeight.Bold)
        topics.forEach { topic ->
            Button(onClick = { onTopicSelected(topic) }) {
                Text(text = topic)
            }
        }
    }
}

@Composable
fun ChatHistory(
    messages: List<ChatMessage>
) {
    Column {
        messages.forEach { message ->
            val backgroundColor =
                if (message.sender == ChatSender.User) Color.LightGray else Color.White

            Text(
                text = message.message,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        shape = RoundedCornerShape(8.dp)
                    ),
                textAlign = if (message.sender == ChatSender.User) TextAlign.End else TextAlign.Start
            )
        }
    }
}

@Composable
fun ResponseInput(
    question: String,
    options: List<String>?,
    onOptionSelected: (String) -> Unit,
    onTextSubmitted: (String) -> Unit
) {
    Column {
        Text(text = question, fontWeight = FontWeight.Bold)

        if(options != null) {
            options.forEach { option ->
                Button(
                    onClick = { onOptionSelected(option) }
                ) {
                    Text(option)
                }
            }
        } else {
            var inputText by remember { mutableStateOf("") }
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Escribe tu respuesta") }
            )
            Button(onClick = { onTextSubmitted(inputText) }) {
                Text("Enviar")
            }
        }
    }
}