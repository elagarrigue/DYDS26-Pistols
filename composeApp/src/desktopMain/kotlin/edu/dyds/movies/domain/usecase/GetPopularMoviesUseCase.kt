package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MoviesRepository

class GetPopularMoviesUseCase(private val repository: MoviesRepository) {
    private val minVoteAverage = 6.0

    suspend operator fun invoke(): List<QualifiedMovie> =
        repository.getPopularMovies()
            .sortedByDescending { it.voteAverage }
            .map { QualifiedMovie(it, it.voteAverage >= minVoteAverage) }
}

