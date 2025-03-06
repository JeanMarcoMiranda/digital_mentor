package com.jacket.digital_mentor.data.repository

import com.jacket.digital_mentor.data.model.VideoDto
import com.jacket.digital_mentor.data.model.toDomain
import com.jacket.digital_mentor.domain.model.Video
import com.jacket.digital_mentor.domain.repository.VideoRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class VideoRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : VideoRepository {
    override suspend fun getVideos(): Result<List<Video>> {
        return try {
            val columnsToSelect = Columns.raw(
                """
                    id,
                    title,
                    youtube_video_id,
                    youtube_video_image,
                    description
                """.trimIndent()
            )

            val result = supabaseClient.from("videos").select(columnsToSelect)

            val videosDto = result.decodeList<VideoDto>()

            val videos = videosDto.map { videoDto -> videoDto.toDomain() }

            return Result.success(videos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}