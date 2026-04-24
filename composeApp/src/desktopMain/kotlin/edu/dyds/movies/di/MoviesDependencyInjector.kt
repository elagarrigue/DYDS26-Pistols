package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.TMDB
import edu.dyds.movies.data.local.Cache
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.presentation.home.HomeViewModel
import edu.dyds.movies.presentation.detail.DetailViewModel

object MoviesDependencyInjector {

    private val remoteDataSource = TMDB()

    private val localDataSource = Cache()

    private val moviesRepository: MoviesRepository =
        MoviesRepositoryImpl(localDataSource, remoteDataSource)

    private val getPopularMoviesUseCase =
        GetPopularMoviesUseCase(moviesRepository)

    private val getMovieDetailsUseCase =
        GetMovieDetailsUseCase(moviesRepository)

    @Composable
    fun getHomeViewModel(): HomeViewModel =
        viewModel { HomeViewModel(getPopularMoviesUseCase) }

    @Composable
    fun getDetailViewModel(): DetailViewModel =
        viewModel { DetailViewModel(getMovieDetailsUseCase) }
}

