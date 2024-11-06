package com.example.digital_mentor.presentation.intent

import com.example.digital_mentor.domain.model.Category

sealed class IlliteracyTestState {
    object Loading : IlliteracyTestState()
    data class Success(val message: String) : IlliteracyTestState()
    data class Error(val message: String) : IlliteracyTestState()
    data class Categories(
        val categories: List<Category>,
        val currentCategoryIndex: Int,
        val currentQuestionIndex: Int,
        val score: Int = 0,
        val selectedAnswer: Boolean? = null
    ) : IlliteracyTestState()
}

sealed class IlliteracyTestIntent {
    object NextQuestion : IlliteracyTestIntent()
    object LoadCategories : IlliteracyTestIntent()
    data class SelectAnswer(val answer: Boolean) : IlliteracyTestIntent()
}