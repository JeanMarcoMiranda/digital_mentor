package com.jacket.digital_mentor.presentation.intent

import com.jacket.digital_mentor.domain.model.Video

sealed class VideoTutorialsState {
    object  Loading : VideoTutorialsState()
    data class Error(val message: String) : VideoTutorialsState()

    data class VideoSelection(
        val videos: List<Video>,
        val searchQuery: String
    ): VideoTutorialsState()

    data class VideoDetail(
        val selectedVideo: Video
    ): VideoTutorialsState()
}

sealed class VideoTutorialsIntent {
    data class onSearchTextChange(val searchText: String) : VideoTutorialsIntent()
    data class onVideoSelected(val video: Video) : VideoTutorialsIntent()
    object onVideoTutorialsClicked : VideoTutorialsIntent()
}