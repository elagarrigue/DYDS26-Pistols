package edu.dyds.movies.data.external.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbRemoteResult(
    val page: Int,
    val results: List<TmdbRemoteMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)
