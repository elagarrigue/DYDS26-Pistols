package edu.dyds.movies.data.fakes

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.domain.entity.Movie

class MovieDetailExternalSourceFake : MovieDetailExternalSource {
    var result: Movie? = null
    var shouldThrow = false

    override suspend fun getMovieByTitle(title: String): Movie? {
        if (shouldThrow) throw RuntimeException("source error")
        return result
    }
}
