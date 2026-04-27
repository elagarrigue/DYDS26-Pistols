package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

class LocalDataSourceImpl : LocalDataSource {
    private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun isNotEmpty(): Boolean = cacheMovies.isNotEmpty()

    override fun getCachedPopularMovies(): List<Movie> = cacheMovies.toList()

    override fun clear() { cacheMovies.clear() }

    override fun addAll(elements: Collection<Movie>) { cacheMovies.addAll(elements) }
}