package com.example.digital_mentor.presentation.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.digital_mentor.domain.model.Category
import com.example.digital_mentor.presentation.intent.IlliteracyTestIntent
import com.example.digital_mentor.presentation.intent.IlliteracyTestState
import com.example.digital_mentor.presentation.viewmodel.IlliteracyTestViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun IlliteracyTestScreen(
    viewModel: IlliteracyTestViewModel = koinViewModel()
) {
    // Obtenemos el estado actual desde el ViewModel
    val viewState by viewModel.viewState.collectAsState()

    // Lanza la petición de datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.sendIntent(IlliteracyTestIntent.LoadCategories)
    }

    when (val state = viewState) {
        is IlliteracyTestState.Loading -> {
            // Mostrar una vista de carga
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        is IlliteracyTestState.Error -> {
            // Mostrar un mensaje de error
            Text(
                text = state.message,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                color = Color.Red
            )
        }

        is IlliteracyTestState.Categories -> {
            // Mostrar la categoría actual y la pregunta
            IlliteracyTestContent(
                categories = state.categories,
                currentCategoryIndex = state.currentCategoryIndex,
                currentQuestionIndex = state.currentQuestionIndex,
                selectedAnswer = state.selectedAnswer,
                onSelectAnswer = { answer ->
                    viewModel.sendIntent(IlliteracyTestIntent.SelectAnswer(answer))
                },
                onNextQuestion = {
                    viewModel.sendIntent(IlliteracyTestIntent.NextQuestion)
                }
            )
        }

        is IlliteracyTestState.Success -> {
            Text(
                text = state.message,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun IlliteracyTestContent(
    categories: List<Category>,
    currentCategoryIndex: Int,
    currentQuestionIndex: Int,
    selectedAnswer: Boolean?,
    onSelectAnswer: (Boolean) -> Unit,
    onNextQuestion: () -> Unit
) {
    val currentCategory = categories.getOrNull(currentCategoryIndex)
    val currentQuestion = currentCategory?.questions?.getOrNull(currentQuestionIndex)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (currentCategory != null && currentQuestion != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.man_image),
                    contentDescription = "algo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(Modifier.height(20.dp))

                // Título de la categoría
                Text(
                    text = currentCategory.name,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                )

                // Pregunta
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp)) // Aplica el borde redondeado
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = currentQuestion.question,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }


                Spacer(Modifier.height(20.dp))

                // Opciones de respuesta
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAnswer == true,
                            onClick = { onSelectAnswer(true) }
                        )
                        Text(text = "Sí")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAnswer == false,
                            onClick = { onSelectAnswer(false) }
                        )
                        Text(text = "No")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


            }

            Button(
                onClick = onNextQuestion,
                enabled = selectedAnswer != null,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = "Siguiente pregunta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        } else {
            // Mensaje en caso de que no haya preguntas
            Text(
                text = "No hay preguntas disponibles",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                color = Color.Gray
            )
        }
    }
}