package com.jacket.digital_mentor.domain.repository

import com.jacket.digital_mentor.domain.model.Topic

interface TopicRepository {
    suspend fun getTopicsWithQuestions(): Result<List<Topic>>
}