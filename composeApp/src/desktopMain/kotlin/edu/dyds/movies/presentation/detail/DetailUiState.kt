package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.error.AppError

data class DetailUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val error: AppError? = null
)

