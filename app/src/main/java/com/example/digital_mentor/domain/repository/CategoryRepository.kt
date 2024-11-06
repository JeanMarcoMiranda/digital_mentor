package com.example.digital_mentor.domain.repository

import com.example.digital_mentor.domain.model.Category

interface CategoryRepository {
    suspend fun getCategoriesWithQuestions(): Result<List<Category>>
}