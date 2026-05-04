package edu.dyds.movies.data.external

interface RemoteDataSource {
    suspend fun getMovieDetails(id: Int): RemoteMovie

    suspend fun getPopularMovies(): RemoteResult
}