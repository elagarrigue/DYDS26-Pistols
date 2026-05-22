package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.local.LocalDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: MoviesExternalSource
) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val localMovies = localDataSource.getPopularMovies()
        if (localMovies.isNotEmpty()) return localMovies

        return try {
            val movies = remoteDataSource.getPopularMovies()
            localDataSource.savePopularMovies(movies)
            movies
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getMovieDetail(title: String): Movie? =
        try {
            remoteDataSource.getMovieByTitle(title)
        } catch (e: Exception) {
            null
        }
}
