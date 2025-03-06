package com.jacket.digital_mentor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacket.digital_mentor.domain.model.Topic
import com.jacket.digital_mentor.domain.model.TopicQuestion
import com.jacket.digital_mentor.domain.usecase.topic.GetTopicWithQuestionsUseCase
import com.jacket.digital_mentor.presentation.intent.ChatMessage
import com.jacket.digital_mentor.presentation.intent.ChatSender
import com.jacket.digital_mentor.presentation.intent.LiveSupportIntent
import com.jacket.digital_mentor.presentation.intent.LiveSupportState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LiveSupportViewModel(
    private val getTopics: GetTopicWithQuestionsUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<LiveSupportState>(
        LiveSupportState.Start
    )
    val viewState: StateFlow<LiveSupportState> = _viewState.asStateFlow()

    private val intentChannel = Channel<LiveSupportIntent> { Channel.UNLIMITED }

    private var selectedTopic: Topic? = null
    private var currentQuestionIndex = 0

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
                    LiveSupportIntent.StartConsulting -> fetchTopics()
                    is LiveSupportIntent.SelectTopic -> handleSelectTopic(intent.topic)
                    is LiveSupportIntent.AnswerQuestion -> handleAnswer(intent)
                    is LiveSupportIntent.UpdateTypeAnswer -> updateTypedAnswer(intent.typeAnswer)
                }
            }
        }
    }

    private fun fetchTopics() {
        viewModelScope.launch {
            _viewState.value = LiveSupportState.Loading

            getTopics().onSuccess { topics ->
                _viewState.value = LiveSupportState.TopicSelection(topics = topics)
            }.onFailure {
                _viewState.value =
                    LiveSupportState.Error(message = "No se pudieron cargar los datos correctamente")
            }
        }
    }

    private fun handleSelectTopic(topic: Topic) {
        selectedTopic = topic
        currentQuestionIndex = 0

        val firstQuestion = topic.topicQuestions.firstOrNull()
        if (firstQuestion != null) {
            updateState {
                LiveSupportState.QuestionConsult(
                    topic = topic,
                    currentQuestion = firstQuestion,
                    messages = listOf(
                        ChatMessage(ChatSender.User, "Topico seleccionado: ${topic.name}"),
                        ChatMessage(ChatSender.System, firstQuestion.questionText)
                    )
                )
            }

        } else {
            updateState {
                LiveSupportState.Error("El tópico no tiene preguntas")
            }
        }
    }

    private fun handleAnswer(intent: LiveSupportIntent.AnswerQuestion) {
        val currentState = _viewState.value
        if (currentState !is LiveSupportState.QuestionConsult) return

        val newMessages = mutableListOf<ChatMessage>()

        // Agregar el mensaje del usuario
        newMessages.add(ChatMessage(ChatSender.User, intent.answer))

        // Verificar si la opción seleccionada necesita más detalles
        val selectedOption =
            currentState.currentQuestion.options.firstOrNull { it.id == intent.optionId }
        if (selectedOption?.needDescription == true) {
            newMessages.add(ChatMessage(ChatSender.System, "Por favor, proporciona más detalles:"))
            updateState {
                currentState.copy(
                    messages = currentState.messages + newMessages,
                    showTextField = true,
                    typedAnswer = ""
                )
            }
        } else {
            // Avanzar a la siguiente pregunta o finalizar la consulta
            val updatedState = goToNextQuestionWithMessages(currentState, newMessages)

            // Actualizar el estado
            updateState { updatedState }
        }
    }

    private fun goToNextQuestionWithMessages(
        currentState: LiveSupportState.QuestionConsult,
        messages: MutableList<ChatMessage>
    ): LiveSupportState.QuestionConsult {
        val topic = currentState.topic
        val nextQuestion = topic.topicQuestions.getOrNull(currentQuestionIndex + 1)

        return if (nextQuestion != null) {
            // Avanzar a la siguiente pregunta
            currentQuestionIndex++
            messages.add(ChatMessage(ChatSender.System, nextQuestion.questionText))
            currentState.copy(
                currentQuestion = nextQuestion,
                messages = currentState.messages + messages,
                showTextField = false,
                typedAnswer = ""
            )
        } else {
            // Conectar con mentor si no hay más preguntas
            connectToMentorWithMessages(currentState, messages)
        }
    }

    private fun connectToMentorWithMessages(
        currentState: LiveSupportState.QuestionConsult,
        messages: MutableList<ChatMessage>
    ): LiveSupportState.QuestionConsult {
        messages.add(
            ChatMessage(
                ChatSender.System,
                "Te conectaremos con uno de nuestros mentores en línea para ayudarte a resolver este problema."
            )
        )
        return currentState.copy(
            messages = currentState.messages + messages,
            showTextField = false,
            showHomeButton = true,
            typedAnswer = ""
        )
    }

    private fun updateTypedAnswer(newAnswer: String) {
        val currentState = _viewState.value as? LiveSupportState.QuestionConsult ?: return
        updateState { currentState.copy(typedAnswer = newAnswer) }
    }

    private fun updateState(newState: () -> LiveSupportState) {
        _viewState.value = newState()
    }
}