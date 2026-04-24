package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteDataSource
import edu.dyds.movies.data.local.LocalDataSource
import edu.dyds.movies.data.mapper.toAppResult
import edu.dyds.movies.data.mapper.toDomain
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import edu.dyds.movies.domain.result.AppResult

class MoviesRepositoryImpl(private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override suspend fun getPopularMovies(): AppResult<List<Movie>> =
        if (localDataSource.isNotEmpty()) {
            AppResult.Success(localDataSource.getCachedPopularMovies())
        } else {
            runCatching { remoteDataSource.getPopularMovies() }
                .map { result ->
                    val movies = result.results.map {it.toDomain()}
                    localDataSource.addAll(movies)
                    movies
            }
                .toAppResult()
        }

    override suspend fun getMovieDetail(id: Int): AppResult<Movie> =
        runCatching { remoteDataSource.getMovieDetails(id).toDomain() }
            .toAppResult()
}

