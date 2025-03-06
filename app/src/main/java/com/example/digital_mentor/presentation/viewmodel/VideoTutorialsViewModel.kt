package com.jacket.digital_mentor.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacket.digital_mentor.domain.usecase.video.GetVideosUseCase
import com.jacket.digital_mentor.presentation.intent.VideoTutorialsIntent
import com.jacket.digital_mentor.presentation.intent.VideoTutorialsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VideoTutorialsViewModel(
    private val getVideos: GetVideosUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<VideoTutorialsState>(
        VideoTutorialsState.VideoSelection(videos = emptyList(), searchQuery = "")
    )

    val viewState: StateFlow<VideoTutorialsState> = _viewState
        .onStart {
            fetchVideosData()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            VideoTutorialsState.Loading
        )

    private val intentChannel = Channel<VideoTutorialsIntent> { Channel.UNLIMITED }

    init {
        handleIntents()
    }

    fun sendIntent(intent: VideoTutorialsIntent) {
        viewModelScope.launch {
            intentChannel.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect() { intent ->
                when (intent) {
                    is VideoTutorialsIntent.onSearchTextChange -> {
                        val currentState = _viewState.value as? VideoTutorialsState.VideoSelection
                            ?: VideoTutorialsState.VideoSelection(
                                videos = emptyList(),
                                searchQuery = ""
                            )

                        _viewState.value = currentState.copy(searchQuery = intent.searchText)
                    }

                    is VideoTutorialsIntent.onVideoSelected -> {
                        _viewState.value =
                            VideoTutorialsState.VideoDetail(selectedVideo = intent.video)
                    }

                    VideoTutorialsIntent.onVideoTutorialsClicked -> {
                        fetchVideosData()
                    }
                }
            }
        }
    }

    private fun fetchVideosData() {
        viewModelScope.launch {
            _viewState.value = VideoTutorialsState.Loading

            getVideos().fold(
                onSuccess = { videos ->
                    Log.d("Videos", "This are the videos: $videos")
                    _viewState.value =
                        VideoTutorialsState.VideoSelection(videos = videos, searchQuery = "")
                },
                onFailure = {
                    _viewState.value =
                        VideoTutorialsState.Error("Error al cargar los datos de los videos")
                }
            )
        }
    }
}