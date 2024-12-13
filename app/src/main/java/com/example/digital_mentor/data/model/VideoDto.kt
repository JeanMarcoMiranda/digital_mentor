package com.example.digital_mentor.data.model

import com.example.digital_mentor.domain.model.Video as DomainVideo
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val id: Int,
    val title: String,
    val youtubeVideoId: String,
    val youtubeVideoImage: String,
    val description: String,
)

fun VideoDto.toDomain(): DomainVideo {
    return DomainVideo(
        id = id,
        title = title,
        youtubeVideoId = youtubeVideoId,
        youtubeVideoImage = youtubeVideoImage,
        description = description
    )
}