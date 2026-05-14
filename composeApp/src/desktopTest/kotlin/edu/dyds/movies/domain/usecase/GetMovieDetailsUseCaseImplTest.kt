package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GetMovieDetailsUseCaseImplTest {

    @Test
    fun `invoke returns movie from repository`() = runTest {
        // arrange
        val repository = MoviesRepositoryFake()
        val movie = movieOf(id = 10, voteAverage = 7.5)
        repository.movieDetail = movie
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(10)

        // assert
        assertEquals(movie, result)
    }

    @Test
    fun `invoke returns null when repository returns null`() = runTest {
        // arrange
        val repository = MoviesRepositoryFake()
        repository.movieDetail = null
        val useCase = GetMovieDetailsUseCaseImpl(repository)

        // act
        val result = useCase(99)

        // assert
        assertNull(result)
    }

    private fun movieOf(id: Int, voteAverage: Double) = Movie(
        id = id,
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

    private class MoviesRepositoryFake : MoviesRepository {
        var popularMovies: List<Movie> = emptyList()
        var movieDetail: Movie? = null

        override suspend fun getPopularMovies(): List<Movie> = popularMovies

        override suspend fun getMovieDetail(id: Int): Movie? = movieDetail
    }
}

