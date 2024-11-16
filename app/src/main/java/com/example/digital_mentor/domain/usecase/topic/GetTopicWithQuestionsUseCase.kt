package com.example.digital_mentor.domain.usecase.topic

import com.example.digital_mentor.domain.model.Topic
import com.example.digital_mentor.domain.repository.TopicRepository

class GetTopicWithQuestionsUseCase(
    private val repository: TopicRepository
) {
    suspend operator fun invoke(): Result<List<Topic>> {
        return try {
            val topicsResult = repository.getTopicsWithQuestions()

            topicsResult.onSuccess {
                val topics = topicsResult.getOrNull() ?: throw Exception("Topics were not found")
                Result.success(topics)
            }.onFailure {
                throw Exception(topicsResult.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}