package edu.dyds.movies.data.external.omdb

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.domain.entity.Movie

internal class OMDBMovieProxy(
    private val source: OMDBMoviesExternalSource
) : MovieDetailExternalSource {

    override suspend fun getMovieByTitle(title: String): Movie? {
        val remote = source.getMovieByTitle(title)
        if (remote.response != "True") return null
        return remote.toDomainMovie()
    }
}
