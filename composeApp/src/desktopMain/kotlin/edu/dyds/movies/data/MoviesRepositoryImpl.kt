package edu.dyds.movies.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.data.external.toDomain
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val client: HttpClient) : MoviesRepository {

    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    override suspend fun getPopularMovies(): List<Movie> =
        if (cacheMovies.isNotEmpty()) {
            cacheMovies.map { it.toDomain() }
        } else {
            try {
                val remoteResult = getTMDBPopularMovies()
                cacheMovies.clear()
                cacheMovies.addAll(remoteResult.results)
                remoteResult.results.map { it.toDomain() }
            } catch (e: Exception) {
                emptyList()
            }
        }

    override suspend fun getMovieDetail(id: Int): Movie? =
        try {
            getTMDBMovieDetails(id).toDomain()
        } catch (e: Exception) {
            null
        }

    private suspend fun getTMDBMovieDetails(id: Int): RemoteMovie =
        client.get("/3/movie/$id").body()

    private suspend fun getTMDBPopularMovies(): RemoteResult =
        client.get("/3/discover/movie?sort_by=popularity.desc").body()
}

