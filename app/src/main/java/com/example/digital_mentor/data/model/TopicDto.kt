package com.jacket.digital_mentor.data.model

import com.jacket.digital_mentor.domain.model.Topic as DomainTopic
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    val id: String,
    val name: String,
    val description: String?,
    val topic_questions: List<TopicQuestionDto>
)

fun TopicDto.toDomain(): DomainTopic {
    return DomainTopic(
        id = id,
        name = name,
        description = description,
        topicQuestions = topic_questions.map { it.toDomain() }
    )
}

