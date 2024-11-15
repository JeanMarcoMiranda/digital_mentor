package com.example.digital_mentor.presentation.intent

sealed class LiveSupportState {
    object Start : LiveSupportState()
    object Loading : LiveSupportState()
    data class Success(val message: String) : LiveSupportState()
    data class Error(val message: String) : LiveSupportState()
    data class ConsultChat(
        val chatHistory: List<ChatMessage>,
    ) : LiveSupportState()

    data class SelectedTopic(
        val topic: String,
        val chatHistory: List<ChatMessage>
    ) : LiveSupportState()

    data class Question(
        val questionText: String,
        val options: List<String>? = null
    ) : LiveSupportState()

    data class inputResponse(val inputText: String) : LiveSupportState()

}

data class ChatMessage(
    val sender: ChatSender,
    val message: String
)

enum class ChatSender {
    User, System
}

sealed class LiveSupportIntent {
    object StartConsulting : LiveSupportIntent() // Inicia la conversación

    // Intención de seleccionar un tópico
    data class SelectTopic(val topic: String) : LiveSupportIntent()

    // Intención de responder a una pregunta en el chat
    data class AnswerQuestion(val answer: String) : LiveSupportIntent()
}