package edu.dyds.movies.data.external

import edu.dyds.movies.data.fakes.MovieExternalSourceFake
import edu.dyds.movies.domain.entity.Movie
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MovieExternalSourceBrokerTest {
    private lateinit var tmdbSource: MovieExternalSourceFake
    private lateinit var omdbSource: MovieExternalSourceFake
    private lateinit var broker: MovieExternalSourceBroker

    @BeforeTest
    fun setUp() {
        tmdbSource = MovieExternalSourceFake()
        omdbSource = MovieExternalSourceFake()
        broker = MovieExternalSourceBroker(tmdbSource, omdbSource)
    }

    @Test
    fun `given both sources return movies, when getMovieByTitle, then returns combined movie`() = runTest {
        // arrange
        val tmdbMovie = movieOf(id = 1, overview = "TMDB overview", popularity = 6.0, voteAverage = 8.0)
        val omdbMovie = movieOf(id = 2, overview = "OMDB overview", popularity = 4.0, voteAverage = 6.0)
        tmdbSource.result = tmdbMovie
        omdbSource.result = omdbMovie

        // act
        val result = broker.getMovieByTitle("Movie 1")

        // assert
        assertEquals(tmdbMovie.title, result.title)
        assertTrue(result.overview.contains("TMDB: TMDB overview"))
        assertTrue(result.overview.contains("OMDB: OMDB overview"))
        assertEquals(5.0, result.popularity)
        assertEquals(7.0, result.voteAverage)
    }

    @Test
    fun `given only TMDB returns a movie, when getMovieByTitle, then returns TMDB movie with prefix`() = runTest {
        // arrange
        val tmdbMovie = movieOf(id = 1, overview = "TMDB overview")
        tmdbSource.result = tmdbMovie
        omdbSource.shouldThrow = true

        // act
        val result = broker.getMovieByTitle("Movie 1")

        // assert
        assertEquals(tmdbMovie.copy(overview = "TMDB: TMDB overview"), result)
    }

    @Test
    fun `given only OMDB returns a movie, when getMovieByTitle, then returns OMDB movie with prefix`() = runTest {
        // arrange
        val omdbMovie = movieOf(id = 2, overview = "OMDB overview")
        tmdbSource.shouldThrow = true
        omdbSource.result = omdbMovie

        // act
        val result = broker.getMovieByTitle("Movie 2")

        // assert
        assertEquals(omdbMovie.copy(overview = "OMDB: OMDB overview"), result)
    }

    @Test
    fun `given neither source returns a movie, when getMovieByTitle, then throws`() = runTest {
        // arrange
        tmdbSource.shouldThrow = true
        omdbSource.shouldThrow = true

        // act & assert
        assertFailsWith<NoSuchElementException> {
            broker.getMovieByTitle("Unknown Movie")
        }
    }

    private fun movieOf(
        id: Int,
        overview: String = "Overview $id",
        popularity: Double = 5.0,
        voteAverage: Double = 7.0
    ) = Movie(
        title = "Movie $id",
        overview = overview,
        releaseDate = "2024-01-01",
        poster = "https://example.com/poster$id.jpg",
        backdrop = null,
        originalTitle = "Movie $id",
        originalLanguage = "en",
        popularity = popularity,
        voteAverage = voteAverage
    )
}
