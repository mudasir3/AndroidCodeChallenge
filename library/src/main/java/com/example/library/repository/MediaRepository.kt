package com.example.library.repository

import com.example.library.api.TmdbApi
import com.example.library.model.MediaItem

open class MediaRepository(private val api: TmdbApi) {
    suspend fun getSearchResults(query: String): List<MediaItem> {
        return api.search(query).results
    }
}
