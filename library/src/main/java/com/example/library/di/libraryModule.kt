package com.example.library.di

import com.example.library.api.Constants.API_KEY
import com.example.library.api.TmdbApi
import com.example.library.repository.MediaRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val libraryModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(HttpCache)
        }
    }
    single { TmdbApi(get(), apiKey = API_KEY) }
    single { MediaRepository(get()) }
}