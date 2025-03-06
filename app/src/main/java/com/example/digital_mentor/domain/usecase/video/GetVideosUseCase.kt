package com.jacket.digital_mentor.domain.usecase.video

import android.util.Log
import com.jacket.digital_mentor.domain.model.Video
import com.jacket.digital_mentor.domain.repository.VideoRepository

class GetVideosUseCase(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(): Result<List<Video>> {
        return try {
            val videoResult = repository.getVideos()

            videoResult.fold(
                onSuccess = {
                    val videos = videoResult.getOrNull() ?: throw Exception("Videos were not found")
                    Result.success(videos)
                },
                onFailure = {
                    Log.d("Videos", "these is the exception ${videoResult.exceptionOrNull()}")
                    throw Exception(videoResult.exceptionOrNull())
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}