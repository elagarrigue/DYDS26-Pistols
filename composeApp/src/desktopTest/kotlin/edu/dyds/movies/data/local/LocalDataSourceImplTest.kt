package edu.dyds.movies.data.local

import edu.dyds.movies.domain.entity.Movie
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalDataSourceImplTest {
    private lateinit var dataSource: LocalDataSourceImpl

    @BeforeTest
    fun setUp() {
        dataSource = LocalDataSourceImpl()
    }

    @Test
    fun `getPopularMovies returns empty list when no movies saved`() {
        // act
        val result = dataSource.getPopularMovies()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `savePopularMovies persists movies retrievable by getPopularMovies`() {
        // arrange
        val movies = listOf(movieOf(id = 1), movieOf(id = 2))

        // act
        dataSource.savePopularMovies(movies)

        // assert
        assertEquals(movies, dataSource.getPopularMovies())
    }

    @Test
    fun `savePopularMovies accumulates movies across multiple saves`() {
        // arrange
        val first = listOf(movieOf(id = 1))
        val second = listOf(movieOf(id = 2), movieOf(id = 3))

        // act
        dataSource.savePopularMovies(first)
        dataSource.savePopularMovies(second)

        // assert
        assertEquals(first + second, dataSource.getPopularMovies())
    }

    @Test
    fun `getPopularMovies returns a copy does not expose mutable internal state`() {
        // arrange
        dataSource.savePopularMovies(listOf(movieOf(id = 1)))

        // act
        val result = dataSource.getPopularMovies().toMutableList()
        result.clear()

        // assert
        assertEquals(1, dataSource.getPopularMovies().size)
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
