package com.jacket.digital_mentor.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import com.jacket.digital_mentor.R
import com.jacket.digital_mentor.domain.model.Category
import com.jacket.digital_mentor.presentation.intent.IlliteracyTestIntent
import com.jacket.digital_mentor.presentation.intent.IlliteracyTestState
import com.jacket.digital_mentor.presentation.viewmodel.IlliteracyTestViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.binayshaw7777.kotstep.model.StepDefaults
import com.binayshaw7777.kotstep.model.StepStyle
import com.binayshaw7777.kotstep.model.tabHorizontal
import com.binayshaw7777.kotstep.ui.horizontal.HorizontalStepper

@Composable
fun IlliteracyTestScreen(
    viewModel: IlliteracyTestViewModel = koinViewModel(),
    onTestCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // Get the view state from the ViewModel
    val viewState by viewModel.viewState.collectAsState()

    when (val state = viewState) {
        is IlliteracyTestState.Loading -> {
            // Here we show a load screen
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }

        is IlliteracyTestState.Error -> {
            // Here we show an error message
            Text(
                text = state.message,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                color = Color.Red
            )
        }

        is IlliteracyTestState.Categories -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                HorizontalStepper(
                    style = tabHorizontal(
                        totalSteps = state.categories.size,
                        currentStep = state.currentCategoryIndex,
                        stepStyle = StepStyle(
                            colors = StepDefaults(
                                doneContainerColor = MaterialTheme.colorScheme.primary,
                                doneLineColor = MaterialTheme.colorScheme.primary,
                                doneContentColor = Color(0xFF212121),
                                currentContainerColor = Color(0xFF4B81F4),
                                currentLineColor = Color(0xFF4B81F4),
                                todoContainerColor = Color(0xFF50596C),
                                todoContentColor = Color.LightGray,
                                todoLineColor = Color(0xFF50596C)
                            )
                        )
                    )
                )

                Spacer(Modifier.height(16.dp))

                // Show the Illiteracy test based on the categories and questions data
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
        }

        else -> Unit
    }

    LaunchedEffect(viewState) {
        when (viewState) {
            is IlliteracyTestState.Success -> {
                val successMessage = (viewState as IlliteracyTestState.Success).message
                onTestCompleted()
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }

            is IlliteracyTestState.Error -> {
                val errorMessage = (viewState as IlliteracyTestState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
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
                // Test Image
                Image(
                    painter = painterResource(id = R.drawable.man_image),
                    contentDescription = "algo",
                    modifier = Modifier.size(120.dp)
                )

                Spacer(Modifier.height(20.dp))

                // Category title
                AnimatedContent(
                    targetState = currentCategory.name,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = fadeIn(animationSpec = tween(500)),
                            initialContentExit = fadeOut(animationSpec = tween(500))
                        )
                    }
                ) { categoryName ->
                    Text(
                        text = categoryName,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Question label
                AnimatedContent(
                    targetState = currentQuestion.question,
                    transitionSpec = {
                        ContentTransform(
                            targetContentEnter = fadeIn(animationSpec = tween(500)),
                            initialContentExit = fadeOut(animationSpec = tween(500))
                        )
                    }
                ) { questionText ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = questionText,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Answer alternatives
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

            // Button to go to the next question
            Button(
                onClick = onNextQuestion,
                enabled = selectedAnswer != null,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = "Siguiente pregunta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        } else {
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