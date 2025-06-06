package com.example.library.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SearchResponse(
    @SerialName("page")
    val page: Int,
    @SerialName("results")
    val results: List<MediaItem>,
    @SerialName("total_pages")
    val total_pages: Int,
    @SerialName("total_results")
    val total_results: Int
)
