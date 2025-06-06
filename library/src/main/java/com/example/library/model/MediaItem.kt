package com.example.library.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MediaItem(
    val id: Int,
    val name: String? = null,
    @SerialName("original_name")
    val originalName: String? = null,
    val title: String? = null,
    @SerialName("original_title")
    val originalTitle: String? = null,
    val overview: String,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("media_type")
    val mediaType: String,
    val adult: Boolean = false,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    val popularity: Double = 0.0,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("origin_country")
    val originCountry: List<String> = emptyList(),
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val video: Boolean? = null
) {
    val displayTitle: String
        get() = name ?: title ?: "Untitled"

    val releaseDateOrFirstAirDate: String
        get() = releaseDate ?: firstAirDate ?: "Unknown"

    val fullPosterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    val fullBackdropUrl: String?
        get() = backdropPath?.let { "https://image.tmdb.org/t/p/w780$it" }

}

