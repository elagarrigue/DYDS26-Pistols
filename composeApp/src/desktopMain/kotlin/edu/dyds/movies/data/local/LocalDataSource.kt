package edu.dyds.movies.data.local

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.mapper.toDomain
import edu.dyds.movies.domain.entity.Movie

class LocalDataSource {
    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    fun isNotEmpty(): Boolean {
        return cacheMovies.isNotEmpty()
    }

    fun getCachedPopularMovies(): List<Movie> {
        return cacheMovies.map { it.toDomain() }
    }

    fun clear() {
        cacheMovies.clear()
    }

    fun addAll(elements: Collection<RemoteMovie>) {
        cacheMovies.addAll(elements)
    }
}