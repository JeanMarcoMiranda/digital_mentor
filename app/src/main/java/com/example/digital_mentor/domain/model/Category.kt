package com.example.digital_mentor.domain.model

data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val difficultyLevel: Number,
    val questions: List<Question>
)
