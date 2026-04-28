package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.result.AppResult
import edu.dyds.movies.domain.result.map

class GetPopularMoviesUseCaseImpl(private val repository: MoviesRepository): GetPopularMoviesUseCase {
    private val minVoteAverage = 6.0

    override suspend operator fun invoke(): AppResult<List<QualifiedMovie>> =
        repository.getPopularMovies().map { movies ->
            movies.sortedByDescending { it.voteAverage }
                .map { QualifiedMovie(it, it.voteAverage >= minVoteAverage) }
        }
}
