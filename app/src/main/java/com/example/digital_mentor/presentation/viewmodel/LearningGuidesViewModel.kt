package com.example.digital_mentor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.course.GetCoursesUseCase
import com.example.digital_mentor.presentation.intent.LearningGuidesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LearningGuidesViewModel(
    private val getCourses: GetCoursesUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<LearningGuidesState>(
        LearningGuidesState.CourseSelection(courses = emptyList())
    )
    val viewState: StateFlow<LearningGuidesState> = _viewState
        .onStart {
            fetchCoursesData()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LearningGuidesState.Loding
        )

    private fun fetchCoursesData() {
        viewModelScope.launch {
            getCourses().onSuccess { courses ->
                _viewState.value = LearningGuidesState.CourseSelection(courses = courses)
            }.onFailure {
                _viewState.value =
                    LearningGuidesState.Error("Error al cargar los datos de los cursos")
            }
        }
    }

}