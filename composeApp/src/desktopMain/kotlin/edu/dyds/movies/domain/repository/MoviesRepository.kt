package edu.dyds.movies.domain.repository

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.result.AppResult

interface MoviesRepository {
    suspend fun getPopularMovies(): AppResult<List<Movie>>
    suspend fun getMovieDetail(id: Int): AppResult<Movie>
}

