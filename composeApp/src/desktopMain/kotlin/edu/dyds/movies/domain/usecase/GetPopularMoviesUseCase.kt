package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.result.AppResult
import edu.dyds.movies.domain.result.map

class GetPopularMoviesUseCase(private val repository: MoviesRepository) {
    private val minVoteAverage = 6.0

    suspend operator fun invoke(): AppResult<List<QualifiedMovie>> =
        repository.getPopularMovies().map { movies ->
            movies.sortedByDescending { it.voteAverage }
                .map { QualifiedMovie(it, it.voteAverage >= minVoteAverage) }
        }
}
