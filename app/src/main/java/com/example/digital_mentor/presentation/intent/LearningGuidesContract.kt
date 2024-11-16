package com.example.digital_mentor.presentation.intent

import com.example.digital_mentor.domain.model.Course

sealed class LearningGuidesState {
    object Loding : LearningGuidesState()
    data class Error(val message: String) : LearningGuidesState()

    data class CourseSelection(
        val courses: List<Course>,
        val searchQuery: String
    ) : LearningGuidesState()

    data class CourseDetail(
        val selectedCourse: Course
    ) : LearningGuidesState()
}

sealed class LearningGuidesIntent {
    data class OnSearchTextChange(val searchText: String) : LearningGuidesIntent()
    data class onCourseSelected(val course: Course) : LearningGuidesIntent()
}