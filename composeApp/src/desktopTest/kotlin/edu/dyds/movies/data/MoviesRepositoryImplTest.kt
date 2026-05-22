package edu.dyds.movies.data

import edu.dyds.movies.data.external.MoviesExternalSource
import edu.dyds.movies.data.fakes.LocalDataSourceFake
import edu.dyds.movies.domain.entity.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoviesRepositoryImplTest {
    private lateinit var local: LocalDataSourceFake
    private lateinit var remote: MoviesExternalSource
    private lateinit var repository: MoviesRepositoryImpl

    @BeforeTest
    fun setUp() {
        local = LocalDataSourceFake()
        remote = mockk<MoviesExternalSource>()
        repository = MoviesRepositoryImpl(local, remote)
    }

    @Test
    fun `getPopularMovies returns local cache when it is not empty`() = runTest {
        // arrange
        val cached = listOf(movieOf(id = 1), movieOf(id = 2))
        local.cachedMovies = cached

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(cached, result)
        coVerify(exactly = 0) { remote.getPopularMovies() }
    }

    @Test
    fun `getPopularMovies fetches from remote and saves to local when cache is empty`() = runTest {
        // arrange
        val movies = listOf(movieOf(id = 10), movieOf(id = 11))
        coEvery { remote.getPopularMovies() } returns movies

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(2, result.size)
        assertTrue(local.saveWasCalled)
        assertEquals(result, local.cachedMovies)
    }

    @Test
    fun `getPopularMovies returns empty list when remote throws and cache is empty`() = runTest {
        // arrange
        coEvery { remote.getPopularMovies() } throws RuntimeException("network error")

        // act
        val result = repository.getPopularMovies()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieDetail returns movie from external source`() = runTest {
        // arrange
        val movie = movieOf(id = 42)
        coEvery { remote.getMovieByTitle(any()) } returns movie

        // act
        val result = repository.getMovieDetail("Movie 42")

        // assert
        assertEquals(movie, result)
    }

    @Test
    fun `getMovieDetail returns null when remote throws`() = runTest {
        // arrange
        coEvery { remote.getMovieByTitle(any()) } throws RuntimeException("network error")

        // act
        val result = repository.getMovieDetail("Movie 99")

        // assert
        assertNull(result)
    }

    private fun movieOf(id: Int) = Movie(
        id = id,
        title = "Movie $id",
        overview = "Overview $id",
        releaseDate = "2024-01-01",
        poster = "https://example.com/poster$id.jpg",
        backdrop = null,
        originalTitle = "Original $id",
        originalLanguage = "en",
        popularity = 5.0,
        voteAverage = 7.0
    )
}
