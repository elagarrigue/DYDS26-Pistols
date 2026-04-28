package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.result.AppResult

interface GetPopularMoviesUseCase {
    suspend operator fun invoke(): AppResult<List<QualifiedMovie>>
}