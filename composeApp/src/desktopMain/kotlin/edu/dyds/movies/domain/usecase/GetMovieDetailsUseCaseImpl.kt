package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.result.AppResult

class GetMovieDetailsUseCaseImpl(private val repository: MoviesRepository): GetMovieDetailsUseCase {
    override suspend operator fun invoke(id: Int): AppResult<Movie> =
        repository.getMovieDetail(id)
}

