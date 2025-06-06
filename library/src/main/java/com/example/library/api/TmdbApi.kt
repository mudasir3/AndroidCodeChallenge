package com.example.library.api

import android.util.Log
import com.example.library.model.SearchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

open class TmdbApi(private val client: HttpClient, private val apiKey: String) {

    suspend fun search(query: String): SearchResponse {
        val response: HttpResponse = client.get("https://api.themoviedb.org/3/search/multi") {
            parameter("api_key", apiKey)
            parameter("query", query)
        }
        return response.body()
    }
}