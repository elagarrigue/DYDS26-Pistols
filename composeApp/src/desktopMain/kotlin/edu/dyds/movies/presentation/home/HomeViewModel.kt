package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.result.AppResult
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    fun getAllMovies() {
        viewModelScope.launch {
            _uiState.emit(HomeUiState(isLoading = true))
            _uiState.emit( when (val result = getPopularMoviesUseCase()) {
                is AppResult.Success -> HomeUiState( isLoading = false, movies = result.data)
                is AppResult.Failure -> HomeUiState(isLoading = false, error = result.error)
            }
            )
        }
    }
}

