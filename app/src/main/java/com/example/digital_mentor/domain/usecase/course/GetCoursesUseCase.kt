package com.jacket.digital_mentor.domain.usecase.course

import android.util.Log
import com.jacket.digital_mentor.domain.model.Course
import com.jacket.digital_mentor.domain.repository.CourseRepository

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
                Log.d("Courses", "these is the exception ${courseResult.exceptionOrNull()}")
                throw Exception(courseResult.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}