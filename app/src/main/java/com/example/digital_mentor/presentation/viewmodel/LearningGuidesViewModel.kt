package com.example.digital_mentor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.course.GetCoursesUseCase
import com.example.digital_mentor.presentation.intent.LearningGuidesIntent
import com.example.digital_mentor.presentation.intent.LearningGuidesState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LearningGuidesViewModel(
    private val getCourses: GetCoursesUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<LearningGuidesState>(
        LearningGuidesState.CourseSelection(courses = emptyList(), searchQuery = "")
    )
    val viewState: StateFlow<LearningGuidesState> = _viewState
        .onStart {
            fetchCoursesData()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LearningGuidesState.Loding
        )

    private val intentChannel = Channel<LearningGuidesIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: LearningGuidesIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    is LearningGuidesIntent.OnSearchTextChange -> {
                        val currentState =
                            _viewState.value as? LearningGuidesState.CourseSelection
                                ?: LearningGuidesState.CourseSelection(
                                    courses = emptyList(),
                                    searchQuery = ""
                                )

                        _viewState.value = currentState.copy(searchQuery = intent.searchText)
                    }

                    is LearningGuidesIntent.onCourseSelected -> {
                        _viewState.value =
                            LearningGuidesState.CourseDetail(selectedCourse = intent.course)
                    }

                    LearningGuidesIntent.onLearingGuidesClicked -> {
//                        _viewState.value = LearningGuidesState.CourseSelection()
                        fetchCoursesData()
                    }
                }
            }
        }
    }

    private fun fetchCoursesData() {
        viewModelScope.launch {
            _viewState.value = LearningGuidesState.Loding

            getCourses().onSuccess { courses ->
                Log.d("Courses", "This are the courses: ")
                _viewState.value =
                    LearningGuidesState.CourseSelection(courses = courses, searchQuery = "")
            }.onFailure {
                _viewState.value =
                    LearningGuidesState.Error("Error al cargar los datos de los cursos")
            }
        }
    }

}