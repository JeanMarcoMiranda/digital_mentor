package com.jacket.digital_mentor.data.model

import kotlinx.serialization.SerialName
import com.jacket.digital_mentor.domain.model.Video as DomainVideo
import kotlinx.serialization.Serializable

@Serializable
data class VideoDto(
    val id: Int,
    val title: String,
    @SerialName("youtube_video_id") val youtubeVideoId: String,
    @SerialName("youtube_video_image") val youtubeVideoImage: String,
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