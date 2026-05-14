package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.yield
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.delay

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovieDetail emits loading state before fetching`() = runTest {
        // arrange
        val useCase = GetMovieDetailsUseCaseFake()
        useCase.delayMillis = 1
        val viewModel = DetailViewModel(useCase)
        val states = mutableListOf<DetailUiState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        yield()

        // act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()

        // assert
        assertTrue(states.any { it.isLoading })
        job.cancel()
    }

    @Test
    fun `getMovieDetail emits movie on success`() = runTest {
        // arrange
        val useCase = GetMovieDetailsUseCaseFake()
        val movie = movieOf(id = 1)
        useCase.result = movie
        val viewModel = DetailViewModel(useCase)
        val states = mutableListOf<DetailUiState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        yield()

        // act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()

        // assert
        assertFalse(states.last().isLoading)
        assertEquals(movie, states.last().movie)
        job.cancel()
    }

    @Test
    fun `getMovieDetail emits null movie when use case returns null`() = runTest {
        // arrange
        val useCase = GetMovieDetailsUseCaseFake()
        useCase.result = null
        val viewModel = DetailViewModel(useCase)
        val states = mutableListOf<DetailUiState>()
        val job = launch { viewModel.uiState.collect { states.add(it) } }
        yield()

        // act
        viewModel.getMovieDetail(99)
        advanceUntilIdle()

        // assert
        assertFalse(states.last().isLoading)
        assertNull(states.last().movie)
        job.cancel()
    }

//
@Test
fun `getMovieDetail does not crash when use case throws`() = runTest {
    // arrange
    val useCase = GetMovieDetailsUseCaseFake()
    useCase.shouldThrow = true

    val viewModel = DetailViewModel(useCase)

    val states = mutableListOf<DetailUiState>()
    val job = launch {
        viewModel.uiState.collect { states.add(it) }
    }

    yield()

    // act
    viewModel.getMovieDetail(100)
    advanceUntilIdle()

    // assert
    assertFalse(states.last().isLoading)
    assertNull(states.last().movie)

    job.cancel()
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

    private class GetMovieDetailsUseCaseFake : GetMovieDetailsUseCase {
        var result: Movie? = null
        var shouldThrow = false
        var delayMillis: Long = 0

        override suspend fun invoke(id: Int): Movie? {
            if (delayMillis > 0) delay(delayMillis)
            if (shouldThrow) throw RuntimeException("use case error")
            return result
        }
    }
}
