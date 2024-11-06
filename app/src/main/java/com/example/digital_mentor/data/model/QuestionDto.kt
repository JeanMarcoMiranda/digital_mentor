package com.example.digital_mentor.data.model

import kotlinx.serialization.Serializable
import com.example.digital_mentor.domain.model.Question as DomainQuestion

@Serializable
data class QuestionDto(
    val id: Int,
    val question: String
)

fun QuestionDto.toDomain(): DomainQuestion {
    return DomainQuestion(
        id = id,
        question = question
    )
}
