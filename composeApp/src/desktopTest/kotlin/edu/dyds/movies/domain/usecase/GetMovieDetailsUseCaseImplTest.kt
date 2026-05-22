package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GetMovieDetailsUseCaseImplTest {
    private lateinit var repository: MoviesRepository
    private lateinit var useCase: GetMovieDetailsUseCaseImpl

    @BeforeTest
    fun setUp() {
        repository = mockk<MoviesRepository>()
        useCase = GetMovieDetailsUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns movie from repository`() = runTest {
        // arrange
        val movie = movieOf(id = 10, voteAverage = 7.5)
        coEvery { repository.getMovieDetail(any()) } returns movie

        // act
        val result = useCase("Movie 10")

        // assert
        assertEquals(movie, result)
    }

    @Test
    fun `invoke returns null when repository returns null`() = runTest {
        // arrange
        coEvery { repository.getMovieDetail(any()) } returns null

        // act
        val result = useCase("Movie 99")

        // assert
        assertNull(result)
    }

    @Test
    fun `invoke propagates exception from repository`() = runTest {
        // arrange
        coEvery { repository.getMovieDetail(any()) } throws RuntimeException("repository error")

        // act & assert
        assertFailsWith<RuntimeException> {
            useCase("Movie 1")
        }
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
