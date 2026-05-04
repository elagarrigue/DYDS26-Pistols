package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteDataSource
import edu.dyds.movies.data.local.LocalDataSource
import edu.dyds.movies.data.mapper.toDomain
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val localMovies = localDataSource.getPopularMovies()
        if (localMovies.isNotEmpty()) return localMovies

        return try {
            val remoteResult = remoteDataSource.getPopularMovies()
            val movies = remoteResult.results.map { it.toDomain() }
            localDataSource.savePopularMovies(movies)
            movies
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getMovieDetail(id: Int): Movie? =
        try {
            remoteDataSource.getMovieDetails(id).toDomain()
        } catch (e: Exception) {
            null
        }
}
