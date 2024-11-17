package com.example.digital_mentor.data.repository

import com.example.digital_mentor.data.model.CourseDto
import com.example.digital_mentor.data.model.toDomain
import com.example.digital_mentor.domain.model.Course
import com.example.digital_mentor.domain.repository.CourseRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class CourseRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : CourseRepository {
    override suspend fun getCourses(): Result<List<Course>> {
        return try {
            val columnsToSelect = Columns.raw(
                """
                id,
                name,
                image,
                pdf,
                description,
                created_at
            """.trimIndent()
            )

            val result = supabaseClient.from("courses").select(columnsToSelect)

            val coursesDto = result.decodeList<CourseDto>()

            val courses = coursesDto.map { courseDto -> courseDto.toDomain() }

            return Result.success(courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}