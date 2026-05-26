package edu.dyds.movies.data.external.tmdb

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
) {
    suspend fun getPopularMovies(): List<TmdbRemoteMovie> =
        tmdbHttpClient.get("/3/discover/movie?sort_by=popularity.desc").body<TmdbRemoteResult>().results

    suspend fun getMovieByTitle(title: String): TmdbRemoteMovie =
        tmdbHttpClient.get("/3/search/movie?query=$title").body<TmdbRemoteResult>().results.first()

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
