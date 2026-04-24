package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.result.AppResult

class GetMovieDetailsUseCase(private val repository: MoviesRepository) {
    suspend operator fun invoke(id: Int): AppResult<Movie> =
        repository.getMovieDetail(id)
}

