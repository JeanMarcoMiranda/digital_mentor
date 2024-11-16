package com.example.digital_mentor.presentation.intent

import com.example.digital_mentor.domain.model.Option
import com.example.digital_mentor.domain.model.Topic
import com.example.digital_mentor.domain.model.TopicQuestion

sealed class LiveSupportState {
    object Start : LiveSupportState()
    object Loading : LiveSupportState()
    data class Error(val message: String) : LiveSupportState()

    data class TopicSelection(
        val topics: List<Topic>
    ) : LiveSupportState()

    data class QuestionConsult(
        val topic: Topic,
        val currentQuestion: TopicQuestion,
        val messages: List<ChatMessage>,
        val showTextField: Boolean = false,
        val typedAnswer: String = ""
    ) : LiveSupportState()
}

sealed class LiveSupportIntent {
    object StartConsulting : LiveSupportIntent() // Inicia la conversación

    // Intención de seleccionar un tópico
    data class SelectTopic(val topic: Topic) : LiveSupportIntent()

    // Intención de responder a una pregunta en el chat
    data class AnswerQuestion(val answer: String, val optionId: Int?) :
        LiveSupportIntent()

    data class UpdateTypeAnswer(val typeAnswer: String) : LiveSupportIntent()
}


data class ChatMessage(
    val sender: ChatSender,
    val message: String
)

enum class ChatSender {
    User, System
}
