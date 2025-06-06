package com.example.library.di

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
    single { TmdbApi(get(), apiKey = "3d0cda4466f269e793e9283f6ce0b75e") }
    single { MediaRepository(get()) }
}