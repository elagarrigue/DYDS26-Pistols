package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class TMDBMoviesProxy(
    private val source: TMDBMoviesExternalSource
) : PopularMoviesExternalSource, MovieDetailExternalSource {

    override suspend fun getPopularMovies(): List<Movie> =
        source.getPopularMovies().map { it.toDomainMovie() }

    override suspend fun getMovieByTitle(title: String): Movie? =
        try {
            source.getMovieByTitle(title).toDomainMovie()
        } catch (e: NoSuchElementException) {
            null
        }
}
