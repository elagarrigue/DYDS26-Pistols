package edu.dyds.movies.data.external.omdb

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val OMDB_API_KEY = "a96e7f78"

internal class OMDBMoviesExternalSource(
    private val omdbHttpClient: HttpClient
) {
    suspend fun getMovieByTitle(title: String): OmdbRemoteMovie =
        omdbHttpClient.get(urlString = "/?t=$title").body()

    companion object {
        fun create() = OMDBMoviesExternalSource(
            HttpClient {
                install(ContentNegotiation) {
                    json(Json { ignoreUnknownKeys = true })
                }
                install(DefaultRequest) {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "www.omdbapi.com"
                        parameters.append("apikey", OMDB_API_KEY)
                    }
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 5000
                }
            }
        )
    }
}
