package com.jacket.digital_mentor.domain.model

data class Topic (
    val id: String,
    val name: String,
    val description: String?,
    val topicQuestions: List<TopicQuestion>
)