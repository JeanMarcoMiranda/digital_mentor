package com.jacket.digital_mentor.domain.usecase.category

import com.jacket.digital_mentor.domain.model.Category
import com.jacket.digital_mentor.domain.repository.CategoryRepository

class GetCategoriesWithQuestionsUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        return repository.getCategoriesWithQuestions()
    }
}