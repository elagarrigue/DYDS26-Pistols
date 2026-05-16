package edu.dyds.movies.presentation.home

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.fakes.GetPopularMoviesUseCaseFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
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
    fun `getAllMovies emits loading state before fetching`() = runTest {
        // arrange
        val useCase = GetPopularMoviesUseCaseFake()
        useCase.delayMillis = 1
        val viewModel = HomeViewModel(useCase)
        val states = mutableListOf<HomeUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getAllMovies()
        advanceUntilIdle()

        // assert
        assertTrue(states.any { it.isLoading })
    }

    @Test
    fun `getAllMovies emits movies on success`() = runTest {
        // arrange
        val useCase = GetPopularMoviesUseCaseFake()
        val expected = listOf(QualifiedMovie(movieOf(id = 1), isGoodMovie = true))
        useCase.result = expected
        val viewModel = HomeViewModel(useCase)
        val states = mutableListOf<HomeUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getAllMovies()

        // assert
        assertFalse(states.last().isLoading)
        assertEquals(expected, states.last().movies)
    }

    @Test
    fun `getAllMovies emits empty movies when use case returns empty list`() = runTest {
        // arrange
        val useCase = GetPopularMoviesUseCaseFake()
        useCase.result = emptyList()
        val viewModel = HomeViewModel(useCase)
        val states = mutableListOf<HomeUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getAllMovies()

        // assert
        assertFalse(states.last().isLoading)
        assertTrue(states.last().movies.isEmpty())
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
