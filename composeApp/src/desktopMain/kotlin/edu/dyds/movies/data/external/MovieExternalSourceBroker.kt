package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie

class MovieExternalSourceBroker(
    private val tmdbSource: MovieDetailExternalSource,
    private val omdbSource: MovieDetailExternalSource
) : MovieDetailExternalSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
        val tmdbResult = try { tmdbSource.getMovieByTitle(title) } catch (e: Exception) { null }
        val omdbResult = try { omdbSource.getMovieByTitle(title) } catch (e: Exception) { null }

        return when {
            tmdbResult != null && omdbResult != null -> Movie(
                title = tmdbResult.title,
                overview = "TMDB: ${tmdbResult.overview}\n\nOMDB: ${omdbResult.overview}",
                releaseDate = tmdbResult.releaseDate,
                poster = tmdbResult.poster,
                backdrop = tmdbResult.backdrop,
                originalTitle = tmdbResult.originalTitle,
                originalLanguage = tmdbResult.originalLanguage,
                popularity = (tmdbResult.popularity + omdbResult.popularity) / 2.0,
                voteAverage = (tmdbResult.voteAverage + omdbResult.voteAverage) / 2.0
            )
            tmdbResult != null -> tmdbResult.copy(overview = "TMDB: ${tmdbResult.overview}")
            omdbResult != null -> omdbResult.copy(overview = "OMDB: ${omdbResult.overview}")
            else -> null
        }
    }
}
