package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

interface LocalDataSource {
    fun isNotEmpty(): Boolean

    fun getCachedPopularMovies(): List<Movie>

    fun clear()

    fun addAll(elements: Collection<Movie>)
}