package edu.dyds.movies.data

import edu.dyds.movies.data.external.MovieDetailExternalSource
import edu.dyds.movies.data.external.PopularMoviesExternalSource
import edu.dyds.movies.data.local.LocalDataSource
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val popularMoviesSource: PopularMoviesExternalSource,
    private val movieDetailSource: MovieDetailExternalSource
) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> {
        val localMovies = localDataSource.getPopularMovies()
        if (localMovies.isNotEmpty()) return localMovies

        return try {
            val movies = popularMoviesSource.getPopularMovies()
            localDataSource.savePopularMovies(movies)
            movies
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getMovieDetail(title: String): Movie? = movieDetailSource.getMovieByTitle(title)
}
