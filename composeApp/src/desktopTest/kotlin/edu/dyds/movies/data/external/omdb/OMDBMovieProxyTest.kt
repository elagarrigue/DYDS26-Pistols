package edu.dyds.movies.data.external.omdb

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith

class OMDBMovieProxyTest {
    private lateinit var source: OMDBMoviesExternalSource
    private lateinit var proxy: OMDBMovieProxy

    @BeforeTest
    fun setUp() {
        source = mockk()
        proxy = OMDBMovieProxy(source)
    }

    @Test
    fun `given source returns movie with True response, when getMovieByTitle, then returns mapped domain movie`() = runTest {
        // arrange
        val remote = OmdbRemoteMovie(response = "True", title = "Movie 1", plot = "A plot")
        coEvery { source.getMovieByTitle(any()) } returns remote

        // act
        val result = proxy.getMovieByTitle("Movie 1")

        // assert
        assertEquals("Movie 1", result?.title)
        assertEquals("A plot", result?.overview)
    }

    @Test
    fun `given source returns False response, when getMovieByTitle, then returns null`() = runTest {
        // arrange
        coEvery { source.getMovieByTitle(any()) } returns OmdbRemoteMovie(response = "False")

        // act
        val result = proxy.getMovieByTitle("Unknown")

        // assert
        assertNull(result)
    }

    @Test
    fun `given source throws, when getMovieByTitle, then propagates exception`() = runTest {
        // arrange
        coEvery { source.getMovieByTitle(any()) } throws RuntimeException("network error")

        // act & assert
        assertFailsWith<RuntimeException> {
            proxy.getMovieByTitle("Movie 1")
        }
    }
}
