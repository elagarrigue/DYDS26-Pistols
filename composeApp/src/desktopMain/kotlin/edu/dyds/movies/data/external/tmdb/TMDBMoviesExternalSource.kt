package edu.dyds.movies.data.external.tmdb

import edu.dyds.movies.data.external.MovieExternalSource
import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.domain.entity.Movie
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TMDB_API_KEY = "d18da1b5da16397619c688b0263cd281"

internal class TMDBMoviesExternalSource(
    private val tmdbHttpClient: HttpClient
) : MoviesExternalSource, MovieExternalSource {

    override suspend fun getPopularMovies(): List<Movie> =
        getTMDBMovies().results.map { it.toDomainMovie() }

    override suspend fun getMovieByTitle(title: String): Movie =
        getTMDBMovieDetails(title).results.first().toDomainMovie()

    private suspend fun getTMDBMovies(): TmdbRemoteResult =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body()

    private suspend fun getTMDBMovieDetails(title: String): TmdbRemoteResult =
        tmdbHttpClient.get("/3/search/movie?query=$title").body()

    companion object {
        fun create() = TMDBMoviesExternalSource(
            HttpClient {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
                install(DefaultRequest) {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "api.themoviedb.org"
                        parameters.append("api_key", TMDB_API_KEY)
                    }
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 5000
                }
            }
        )
    }
}
