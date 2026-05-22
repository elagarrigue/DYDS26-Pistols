package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p"

@Serializable
data class TmdbRemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
) {
    fun toDomainMovie(): Movie = Movie(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate ?: "",
        poster = posterPath?.let { "$TMDB_IMAGE_BASE_URL/w185$it" } ?: "",
        backdrop = backdropPath?.let { "$TMDB_IMAGE_BASE_URL/w780$it" },
        originalTitle = originalTitle,
        originalLanguage = originalLanguage,
        popularity = popularity ?: 0.0,
        voteAverage = voteAverage ?: 0.0
    )
}
