package com.example.digital_mentor.data.model

import kotlinx.serialization.Serializable
import com.example.digital_mentor.domain.model.Category as DomainCategory

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val description: String,
    val difficulty_level: Int,
    val questions: List<QuestionDto>
)

fun CategoryDto.toDomain(): DomainCategory {
    return DomainCategory(
        id = id,
        name = name,
        description = description,
        difficultyLevel = difficulty_level,
        questions = questions.map { it.toDomain() }
    )
}
