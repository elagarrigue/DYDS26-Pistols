package edu.dyds.movies.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.RemoteDataSourceImpl
import edu.dyds.movies.data.local.LocalDataSourceImpl
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCaseImpl
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCaseImpl
import edu.dyds.movies.presentation.home.HomeViewModel
import edu.dyds.movies.presentation.detail.DetailViewModel

object MoviesDependencyInjector {

    private val remoteDataSource = RemoteDataSourceImpl()

    private val localDataSource = LocalDataSourceImpl()

    private val moviesRepository: MoviesRepository =
        MoviesRepositoryImpl(localDataSource, remoteDataSource)

    private val getPopularMoviesUseCase =
        GetPopularMoviesUseCaseImpl(moviesRepository)

    private val getMovieDetailsUseCase =
        GetMovieDetailsUseCaseImpl(moviesRepository)

    @Composable
    fun getHomeViewModel(): HomeViewModel =
        viewModel { HomeViewModel(getPopularMoviesUseCase) }

    @Composable
    fun getDetailViewModel(): DetailViewModel =
        viewModel { DetailViewModel(getMovieDetailsUseCase) }
}

