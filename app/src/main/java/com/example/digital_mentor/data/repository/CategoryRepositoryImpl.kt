package com.jacket.digital_mentor.data.repository

import android.util.Log
import com.jacket.digital_mentor.data.model.CategoryDto
import com.jacket.digital_mentor.data.model.toDomain
import com.jacket.digital_mentor.domain.model.Category
import com.jacket.digital_mentor.domain.repository.CategoryRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class CategoryRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : CategoryRepository {
    override suspend fun getCategoriesWithQuestions(): Result<List<Category>> {
        return try {
            val columnsToSelect =
                Columns.raw("id, name, description, difficulty_level, questions(id, question)")
            val result =
                supabaseClient.from("categories").select(columnsToSelect)

            val categoriesDto = result.decodeList<CategoryDto>()

            val categories = categoriesDto.map { categoryDto -> categoryDto.toDomain() }

            return Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}