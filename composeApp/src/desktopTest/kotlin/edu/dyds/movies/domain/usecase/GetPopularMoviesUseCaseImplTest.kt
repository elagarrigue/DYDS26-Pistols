package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GetPopularMoviesUseCaseImplTest {
    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetPopularMoviesUseCaseImpl

    @BeforeTest
    fun setUp() {
        repository = mockk<MoviesRepository>()
        useCase = GetPopularMoviesUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns movies sorted by vote average descending`() = runTest {
        // arrange
        val low = movieOf(id = 1, voteAverage = 5.0)
        val high = movieOf(id = 2, voteAverage = 8.0)
        val mid = movieOf(id = 3, voteAverage = 7.0)
        coEvery { repository.getPopularMovies() } returns listOf(low, high, mid)

        // act
        val result = useCase()

        // assert
        assertEquals(listOf("Movie 2", "Movie 3", "Movie 1"), result.map { it.movie.title })
    }

    @Test
    fun `invoke marks movies with voteAverage greater or equal to 6 as good`() = runTest {
        // arrange
        coEvery { repository.getPopularMovies() } returns listOf(movieOf(id = 1, voteAverage = 6.0))

        // act
        val result = useCase()

        // assert
        assertTrue(result.first().isGoodMovie)
    }

    @Test
    fun `invoke marks movies with voteAverage less than 6 as not good`() = runTest {
        // arrange
        coEvery { repository.getPopularMovies() } returns listOf(movieOf(id = 1, voteAverage = 5.9))

        // act
        val result = useCase()

        // assert
        assertFalse(result.first().isGoodMovie)
    }

    @Test
    fun `invoke returns empty list when repository returns empty`() = runTest {
        // arrange
        coEvery { repository.getPopularMovies() } returns emptyList()

        // act
        val result = useCase()

        // assert
        assertTrue(result.isEmpty())
    }

    @Test
    fun `invoke maps all movies to QualifiedMovie preserving movie data`() = runTest {
        // arrange
        val first = movieOf(id = 1, voteAverage = 8.0)
        val second = movieOf(id = 2, voteAverage = 7.0)
        coEvery { repository.getPopularMovies() } returns listOf(first, second)

        // act
        val result = useCase()

        // assert
        assertEquals(listOf(first, second), result.map(QualifiedMovie::movie))
    }

    private fun movieOf(id: Int, voteAverage: Double) = Movie(
        title = "Movie $id",
        overview = "Overview $id",
        releaseDate = "2024-01-01",
        poster = "https://example.com/poster$id.jpg",
        backdrop = null,
        originalTitle = "Original $id",
        originalLanguage = "en",
        popularity = 5.0,
        voteAverage = voteAverage
    )
}
