package edu.dyds.movies.fakes

import edu.dyds.movies.data.local.LocalDataSource
import edu.dyds.movies.domain.entity.Movie

class LocalDataSourceFake: LocalDataSource {
    var cachedMovies: List<Movie> = emptyList()
    var saveWasCalled = false

    override fun getPopularMovies(): List<Movie> = cachedMovies

    override fun savePopularMovies(movies: Collection<Movie>) {
        saveWasCalled = true
        cachedMovies = movies.toList()
    }
}