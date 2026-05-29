package edu.dyds.movies.data.external.tmdb

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TMDBMoviesProxyTest {
    private lateinit var source: TMDBMoviesExternalSource
    private lateinit var proxy: TMDBMoviesProxy

    @BeforeTest
    fun setUp() {
        source = mockk()
        proxy = TMDBMoviesProxy(source)
    }

    @Test
    fun `given source returns list, when getPopularMovies, then returns mapped domain movies`() = runTest {
        // arrange
        val remote = remoteMovieOf(title = "Movie 1", popularity = 7.5, voteAverage = 8.0)
        coEvery { source.getPopularMovies() } returns listOf(remote)

        // act
        val result = proxy.getPopularMovies()

        // assert
        assertEquals(1, result.size)
        assertEquals("Movie 1", result.first().title)
        assertEquals(7.5, result.first().popularity)
        assertEquals(8.0, result.first().voteAverage)
    }

    @Test
    fun `given source returns movie, when getMovieByTitle, then returns mapped domain movie`() = runTest {
        // arrange
        val remote = remoteMovieOf(title = "Movie 1")
        coEvery { source.getMovieByTitle(any()) } returns remote

        // act
        val result = proxy.getMovieByTitle("Movie 1")

        // assert
        assertEquals("Movie 1", result?.title)
    }

    @Test
    fun `given source throws NoSuchElementException, when getMovieByTitle, then returns null`() = runTest {
        // arrange
        coEvery { source.getMovieByTitle(any()) } throws NoSuchElementException()

        // act
        val result = proxy.getMovieByTitle("Unknown")

        // assert
        assertNull(result)
    }

    private fun remoteMovieOf(
        title: String,
        popularity: Double = 5.0,
        voteAverage: Double = 7.0
    ) = TmdbRemoteMovie(
        id = 1,
        title = title,
        overview = "Overview",
        originalTitle = title,
        originalLanguage = "en",
        popularity = popularity,
        voteAverage = voteAverage
    )
}
