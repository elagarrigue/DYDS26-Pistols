package edu.dyds.movies.data

import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.fakes.LocalDataSourceFake
import edu.dyds.movies.fakes.RemoteDataSourceFake
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoviesRepositoryImplTest {

    @Test
    fun `getPopularMovies returns local cache when it is not empty`() = runTest {
        // arrange
        val local = LocalDataSourceFake()
        val remote = RemoteDataSourceFake()
        val cached = listOf(movieOf(id = 1), movieOf(id = 2))
        local.cachedMovies = cached
        val repository = MoviesRepositoryImpl(local, remote)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertEquals(cached, result)
        assertTrue(!remote.getPopularMoviesWasCalled)
    }

    @Test
    fun `getPopularMovies fetches from remote and saves to local when cache is empty`() = runTest {
        // arrange
        val local = LocalDataSourceFake()
        val remote = RemoteDataSourceFake()
        remote.popularMoviesResult = RemoteResult(
            page = 1,
            results = listOf(remoteMovieOf(id = 10), remoteMovieOf(id = 11)),
            totalPages = 1,
            totalResults = 2
        )
        val repository = MoviesRepositoryImpl(local, remote)

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
        val local = LocalDataSourceFake()
        val remote = RemoteDataSourceFake()
        remote.shouldThrowOnPopular = true
        val repository = MoviesRepositoryImpl(local, remote)

        // act
        val result = repository.getPopularMovies()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getMovieDetail returns mapped movie from remote`() = runTest {
        // arrange
        val local = LocalDataSourceFake()
        val remote = RemoteDataSourceFake()
        remote.movieDetailResult = remoteMovieOf(id = 42, posterPath = "/poster.jpg", backdropPath = "/backdrop.jpg")
        val repository = MoviesRepositoryImpl(local, remote)

        // act
        val result = repository.getMovieDetail(42)

        // assert
        requireNotNull(result)
        assertEquals(42, result.id)
        assertEquals("Movie 42", result.title)
        assertTrue(result.poster.startsWith("https://image.tmdb.org/t/p/w185"))
        assertTrue(result.backdrop?.startsWith("https://image.tmdb.org/t/p/w780") == true)
    }

    @Test
    fun `getMovieDetail returns null when remote throws`() = runTest {
        // arrange
        val local = LocalDataSourceFake()
        val remote = RemoteDataSourceFake()
        remote.shouldThrowOnDetail = true
        val repository = MoviesRepositoryImpl(local, remote)

        // act
        val result = repository.getMovieDetail(99)

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

    private fun remoteMovieOf(
        id: Int,
        posterPath: String = "/poster$id.jpg",
        backdropPath: String? = null
    ) = RemoteMovie(
        id = id,
        title = "Movie $id",
        overview = "Overview $id",
        releaseDate = "2024-01-01",
        posterPath = posterPath,
        backdropPath = backdropPath,
        originalTitle = "Original $id",
        originalLanguage = "en",
        popularity = 5.0,
        voteAverage = 7.0
    )
}

