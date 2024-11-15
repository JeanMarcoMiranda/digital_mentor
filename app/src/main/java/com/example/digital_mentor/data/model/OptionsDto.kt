package com.example.digital_mentor.data.model

import com.example.digital_mentor.domain.model.Option as DomainOption
import kotlinx.serialization.Serializable

@Serializable
data class OptionsDto(
    val id: String,
    val question_id: String,
    val option_text: String,
    val need_description: Boolean
)

fun OptionsDto.toDomain(): DomainOption {
    return DomainOption(
        id = id,
        questionId = question_id,
        optionText = option_text,
        needDescription = need_description
    )
}