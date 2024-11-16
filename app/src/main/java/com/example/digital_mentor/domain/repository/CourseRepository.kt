package com.example.digital_mentor.domain.repository

import com.example.digital_mentor.domain.model.Course

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>
}