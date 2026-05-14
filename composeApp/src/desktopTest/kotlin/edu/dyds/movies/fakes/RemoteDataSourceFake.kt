package edu.dyds.movies.fakes

import edu.dyds.movies.data.external.RemoteDataSource
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult

class RemoteDataSourceFake: RemoteDataSource {
    var popularMoviesResult: RemoteResult = RemoteResult(
        page = 1,
        results = emptyList(),
        totalPages = 1,
        totalResults = 0
    )
    var movieDetailResult: RemoteMovie? = null
    var shouldThrowOnPopular = false
    var shouldThrowOnDetail = false
    var getPopularMoviesWasCalled = false

    override suspend fun getPopularMovies(): RemoteResult {
        getPopularMoviesWasCalled = true
        if (shouldThrowOnPopular) throw RuntimeException("network error")
        return popularMoviesResult
    }

    override suspend fun getMovieDetails(id: Int): RemoteMovie {
        if (shouldThrowOnDetail) throw RuntimeException("network error")
        return movieDetailResult ?: throw RuntimeException("not found")
    }
}