package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.result.AppResult

interface GetMovieDetailsUseCase {
    suspend operator fun invoke(id: Int): AppResult<Movie>
}