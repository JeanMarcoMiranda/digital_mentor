package com.jacket.digital_mentor.data.model

import com.jacket.digital_mentor.domain.model.Course as DomainCourse
import kotlinx.serialization.Serializable

@Serializable
data class CourseDto(
    val id: String,
    val name: String,
    val image: String,
    val pdf: String,
    val description: String?,
    val created_at: String
)

fun CourseDto.toDomain(): DomainCourse {
    return DomainCourse(
        id = id,
        name = name,
        image = image,
        pdf = pdf,
        description = description
    )
}