package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteDataSource
import edu.dyds.movies.data.local.LocalDataSource
import edu.dyds.movies.data.mapper.toDomain
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class MoviesRepositoryImpl(private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) : MoviesRepository {

    override suspend fun getPopularMovies(): List<Movie> =
        if (localDataSource.isNotEmpty()) {
            localDataSource.getCachedPopularMovies()
        } else {
            try {
                val remoteResult = remoteDataSource.getTMDBPopularMovies()
                localDataSource.clear()
                localDataSource.addAll(remoteResult.results)
                remoteResult.results.map { it.toDomain() }
            } catch (e: Exception) {
                emptyList()
            }
        }

    override suspend fun getMovieDetail(id: Int): Movie? =
        try {
            remoteDataSource.getTMDBMovieDetails(id).toDomain()
        } catch (e: Exception) {
            null
        }
}

