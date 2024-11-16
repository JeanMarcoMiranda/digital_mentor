package com.example.digital_mentor.domain.usecase.course

import com.example.digital_mentor.domain.model.Course
import com.example.digital_mentor.domain.repository.CourseRepository

class GetCoursesUseCase(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): Result<List<Course>> {
        return try {
            val courseResult = repository.getCourses()

            courseResult.onSuccess {
                val courses = courseResult.getOrNull() ?: throw Exception("Courses were not found")
                Result.success(courses)
            }.onFailure {
                throw Exception(courseResult.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}