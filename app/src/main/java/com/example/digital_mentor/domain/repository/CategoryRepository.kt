package com.jacket.digital_mentor.domain.repository

import com.jacket.digital_mentor.domain.model.Category

interface CategoryRepository {
    suspend fun getCategoriesWithQuestions(): Result<List<Category>>
}