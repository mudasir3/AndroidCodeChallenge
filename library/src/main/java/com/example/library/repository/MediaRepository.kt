package com.example.library.repository

import android.util.Log
import com.example.library.api.TmdbApi
import com.example.library.model.MediaItem
import io.ktor.client.call.body

open class MediaRepository(private val api: TmdbApi) {
    suspend fun getSearchResults(query: String): List<MediaItem> {
        return api.search(query).results
    }
}
