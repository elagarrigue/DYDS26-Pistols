package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class TMDBMoviesProxy(
    private val source: TMDBMoviesExternalSource
) : MoviesExternalSource, MovieExternalSource {

    override suspend fun getPopularMovies(): List<Movie> =
        source.getPopularMovies().map { it.toDomainMovie() }

    override suspend fun getMovieByTitle(title: String): Movie =
        source.getMovieByTitle(title).toDomainMovie()
}
