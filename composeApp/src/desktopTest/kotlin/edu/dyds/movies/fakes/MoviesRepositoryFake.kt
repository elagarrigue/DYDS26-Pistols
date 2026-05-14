package edu.dyds.movies.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryFake: MoviesRepository {
    var popularMovies: List<Movie> = emptyList()
    var movieDetail: Movie? = null

    override suspend fun getPopularMovies(): List<Movie> = popularMovies

    override suspend fun getMovieDetail(id: Int): Movie? = movieDetail
}