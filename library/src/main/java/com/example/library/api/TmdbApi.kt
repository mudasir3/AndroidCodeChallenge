package com.example.library.api

import com.example.library.api.Constants.BASE_URL
import com.example.library.model.SearchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

open class TmdbApi(private val client: HttpClient, private val apiKey: String) {

    suspend fun search(query: String): SearchResponse {
        val response: HttpResponse = client.get(BASE_URL) {
            parameter("api_key", apiKey)
            parameter("query", query)
        }
        return response.body()
    }
}