package edu.dyds.movies.presentation.home

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.error.AppError

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<QualifiedMovie> = emptyList(),
    val error: AppError? = null
)

