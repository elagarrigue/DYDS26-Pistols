package edu.dyds.movies.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.result.AppResult
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            _uiState.emit(DetailUiState(isLoading = true))
            _uiState.emit( when (val result = getMovieDetailsUseCase(id)) {
                is AppResult.Success -> DetailUiState(isLoading = false, movie = result.data)
                is AppResult.Failure -> DetailUiState(isLoading = false, error = result.error)
            }
            )
        }
    }
}

