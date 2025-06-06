package com.example.starzplay_videoappcodechallenge.ui.main

import com.example.library.model.MediaItem

sealed class MainIntent {
    data class Search(val query: String) : MainIntent()
}

sealed class MainEffect {
    data class ShowError(val message: String) : MainEffect()
}

data class MainViewState(
    val isLoading: Boolean = false,
    val mediaByType: Map<String, List<MediaItem>> = emptyMap()
)