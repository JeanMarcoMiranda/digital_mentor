package com.example.digital_mentor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.presentation.intent.ChatMessage
import com.example.digital_mentor.presentation.intent.ChatSender
import com.example.digital_mentor.presentation.intent.LiveSupportIntent
import com.example.digital_mentor.presentation.intent.LiveSupportState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LiveSupportViewModel(

) : ViewModel() {

    private val _viewState = MutableStateFlow<LiveSupportState>(
        LiveSupportState.Start
    )
    val viewState: StateFlow<LiveSupportState> = _viewState.asStateFlow()

    private val intentChannel = Channel<LiveSupportIntent> { Channel.UNLIMITED }
    private val chatHistory = mutableListOf<ChatMessage>()

    init {
        handleIntents()
    }

    fun sendIntent(intent: LiveSupportIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    LiveSupportIntent.StartConsulting -> {
                        _viewState.value = LiveSupportState.ConsultChat(consultTopics = "alsalf")
                    }

                    is LiveSupportIntent.AnswerQuestion -> TODO()
                    is LiveSupportIntent.SelectTopic -> TODO()
                }
            }
        }
    }

    private fun startConsulting() {
        _viewState.value = LiveSupportState.ConsultChat(
            chatHistory = chatHistory.toList()
        )
    }

    private fun selectTopic(
        topic: String
    ) {
        chatHistory.add(ChatMessage(ChatSender.User, topic))
        chatHistory.add(ChatMessage(ChatSender.System, "¿Cómo podemos ayudarte con $topic?"))

        _viewState.value = LiveSupportState.SelectedTopic(
            topic = topic,
            chatHistory = chatHistory.toList()
        )
    }

    // Lógica para manejar respuestas a las preguntas (opciones o entrada de texto)
    private fun answerQuestion(answer: String) {
        // Agregar la respuesta al historial del chat
        chatHistory.add(ChatMessage(ChatSender.User, answer))

        // Obtener la siguiente pregunta o finalizar
        val nextQuestion = getNextQuestion()
        if (nextQuestion != null) {
            chatHistory.add(ChatMessage(ChatSender.System, nextQuestion.questionText))
            _viewState.value = LiveSupportState.Question(
                questionText = nextQuestion.questionText,
                options = nextQuestion.options
            )
        } else {
            chatHistory.add(ChatMessage(ChatSender.System, "Gracias por usar el soporte en vivo."))
            _viewState.value = LiveSupportState.Success("Bueans")
        }
    }

    // Simulación de lógica para obtener la siguiente pregunta en función del contexto actual
    private fun getNextQuestion(): QuestionData? {
        // Aquí se definiría la lógica para obtener la siguiente pregunta,
        // basada en el tópico seleccionado y las respuestas previas.
        // Esta función devuelve un objeto `QuestionData` o `null` si no hay más preguntas.

        // Ejemplo de pregunta de prueba (se debería obtener dinámicamente)
        return QuestionData(
            questionText = "¿Tienes alguna otra consulta sobre este tema?",
            options = listOf("Sí", "No")
        )
    }
}