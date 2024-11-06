package com.example.digital_mentor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.digital_mentor.domain.usecase.GetCategoriesWithQuestionsUseCase
import com.example.digital_mentor.presentation.intent.IlliteracyTestIntent
import com.example.digital_mentor.presentation.intent.IlliteracyTestState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IlliteracyTestViewModel(
    private val getCategoriesWithQuestionsUseCase: GetCategoriesWithQuestionsUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<IlliteracyTestState>(
        IlliteracyTestState.Categories(
            emptyList(), 0, 0, 0, null
        )
    )
    val viewState: StateFlow<IlliteracyTestState> = _viewState
        .onStart {
            fetchCategoriesData()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            IlliteracyTestState.Loading
        )

    private val intentChannel = Channel<IlliteracyTestIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
        fetchCategoriesData()
    }

    fun sendIntent(intent: IlliteracyTestIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    is IlliteracyTestIntent.SelectAnswer -> {
                        // Update selectedAnswer value
                        if(_viewState.value is IlliteracyTestState.Categories) {
                            val newState = _viewState.value as IlliteracyTestState.Categories
                            _viewState.value = newState.copy(selectedAnswer = intent.answer)
                        }
                    }
                    IlliteracyTestIntent.NextQuestion -> handleNextQuestion()
                    IlliteracyTestIntent.LoadCategories -> fetchCategoriesData()
                }
            }
        }
    }

    private fun handleNextQuestion() {
        val currentState = _viewState.value

        if (currentState is IlliteracyTestState.Categories) {
            val newScore =
                if (currentState.selectedAnswer == true) currentState.score + 1 else currentState.score

            val currentCategoryIndex = currentState.currentCategoryIndex
            val currentQuestionIndex = currentState.currentQuestionIndex
            val categories = currentState.categories

            // Check if there are more questions in the current category
            val currentCategory = categories.getOrNull(currentCategoryIndex)
            val hasNextQuestion = currentCategory?.questions?.size?.let {
                currentQuestionIndex + 1 < it
            } == true

            if (hasNextQuestion) {
                // Go to the next question
                _viewState.value =
                    currentState.copy(
                        score = newScore,
                        selectedAnswer = null,
                        currentQuestionIndex = currentQuestionIndex + 1
                    )
            } else {
                // Check if there are more categories
                val hasNextCategory = currentCategoryIndex + 1 < categories.size

                if (hasNextCategory) {
                    // Move to the next category and reset the question index
                    _viewState.value = currentState.copy(
                        score = newScore,
                        selectedAnswer = null,
                        currentCategoryIndex = currentCategoryIndex + 1,
                        currentQuestionIndex = 0
                    )
                } else {
                    // There are no more categories, show Complete Test
                    _viewState.value =
                        IlliteracyTestState.Success("Test finalizado. Puntaje: ${currentState.score}")
                }
            }

        }
    }

    private fun fetchCategoriesData() {
        viewModelScope.launch {
            _viewState.value = IlliteracyTestState.Loading
            getCategoriesWithQuestionsUseCase().onSuccess { categories ->
                Log.d("QuestionsViewModel", "These are the categories: $categories")

                _viewState.value = IlliteracyTestState.Categories(categories ?: emptyList(), 0, 0)
            }.onFailure {
                Log.d("QuestionsViewModel", "No se pudieron cargar los datos")
                _viewState.value =
                    IlliteracyTestState.Error("Error al cargar los datos de las preguntas")
            }
        }
    }
}