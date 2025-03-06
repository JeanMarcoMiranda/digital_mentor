package com.jacket.digital_mentor.domain.repository

import com.jacket.digital_mentor.domain.model.Course

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>
}