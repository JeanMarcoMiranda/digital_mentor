package com.jacket.digital_mentor.domain.repository

import com.jacket.digital_mentor.domain.model.Video

interface VideoRepository {
    suspend fun getVideos(): Result<List<Video>>
}