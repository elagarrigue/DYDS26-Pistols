package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.fakes.GetMovieDetailsUseCaseFake
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

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
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getMovieDetail(1)
        advanceUntilIdle()

        // assert
        assertTrue(states.any { it.isLoading })
    }

    @Test
    fun `getMovieDetail emits movie on success`() = runTest {
        // arrange
        val useCase = GetMovieDetailsUseCaseFake()
        val movie = movieOf(id = 1)
        useCase.result = movie
        val viewModel = DetailViewModel(useCase)
        val states = mutableListOf<DetailUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getMovieDetail(1)

        // assert
        assertFalse(states.last().isLoading)
        assertEquals(movie, states.last().movie)
    }

    @Test
    fun `getMovieDetail emits null movie when use case returns null`() = runTest {
        // arrange
        val useCase = GetMovieDetailsUseCaseFake()
        useCase.result = null
        val viewModel = DetailViewModel(useCase)
        val states = mutableListOf<DetailUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getMovieDetail(99)

        // assert
        assertFalse(states.last().isLoading)
        assertNull(states.last().movie)
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
