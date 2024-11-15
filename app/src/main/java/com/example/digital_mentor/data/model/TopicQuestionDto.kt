package com.example.digital_mentor.data.model

import com.example.digital_mentor.domain.model.TopicQuestion as DomainTopicQuestion
import kotlinx.serialization.Serializable

@Serializable
data class TopicQuestionDto(
    val id: String,
    val topic_id: String,
    val question_text: String,
    val response_type: String,
    val order: Int,
    val options: List<OptionsDto>
)

fun TopicQuestionDto.toDomain(): DomainTopicQuestion {
    return DomainTopicQuestion(
        id = id,
        topicId = topic_id,
        questionText = question_text,
        responseType = response_type,
        order = order,
        options = options.map { it.toDomain() }
    )
}