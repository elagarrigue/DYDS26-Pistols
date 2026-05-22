package edu.dyds.movies.data.fakes

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.domain.entity.Movie

class MovieExternalSourceFake : MovieExternalSource {
    var result: Movie? = null
    var shouldThrow = false

    override suspend fun getMovieByTitle(title: String): Movie {
        if (shouldThrow) throw RuntimeException("source error")
        return checkNotNull(result) { "result not configured in fake" }
    }
}
