package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface LocalDataSource {
    fun getPopularMovies(): List<Movie>

    fun savePopularMovies(movies: Collection<Movie>)
}