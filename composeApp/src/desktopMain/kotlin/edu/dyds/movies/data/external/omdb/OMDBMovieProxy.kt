package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class OMDBMovieProxy(
    private val source: OMDBMoviesExternalSource
) : MovieExternalSource {

    override suspend fun getMovieByTitle(title: String): Movie {
        val remote = source.getMovieByTitle(title)
        check(remote.response == "True") { "OMDB: movie not found for title: $title" }
        return remote.toDomainMovie()
    }
}
