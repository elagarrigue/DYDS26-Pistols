package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie

class LocalDataSourceImpl : LocalDataSource {
    private val cacheMovies: MutableList<Movie> = mutableListOf()

    override fun getPopularMovies(): List<Movie> = cacheMovies.toList()

    override fun savePopularMovies(movies: Collection<Movie>) { cacheMovies.addAll(movies) }
}