package com.example.digital_mentor.domain.usecase

import com.example.digital_mentor.domain.model.Category
import com.example.digital_mentor.domain.repository.CategoryRepository

class GetCategoriesWithQuestionsUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getCategoriesWithQuestions()
    }
}