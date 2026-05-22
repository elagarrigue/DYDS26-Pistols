package edu.dyds.movies.presentation.home

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.fakes.GetPopularMoviesUseCaseFake
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
    private lateinit var useCase: GetPopularMoviesUseCaseFake
    private lateinit var viewModel: HomeViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = GetPopularMoviesUseCaseFake()
        viewModel = HomeViewModel(useCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given use case has delay, when getAllMovies, then loading state is emitted`() = runTest {
        // arrange
        useCase.delayMillis = 1
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
    fun `given use case returns movies, when getAllMovies, then final state has those movies`() = runTest {
        // arrange
        val expected = listOf(QualifiedMovie(movieOf(id = 1), isGoodMovie = true))
        useCase.result = expected
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
    fun `given use case returns empty list, when getAllMovies, then final state has no movies`() = runTest {
        // arrange
        useCase.result = emptyList()
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
