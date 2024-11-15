package com.example.digital_mentor.domain.model

class TopicQuestion (
    val id: String,
    val topicId: String,
    val questionText: String,
    val responseType: String,
    val order: Int,
    val options: List<Option>
)