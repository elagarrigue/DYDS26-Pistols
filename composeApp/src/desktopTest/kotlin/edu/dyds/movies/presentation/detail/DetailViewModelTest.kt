package edu.dyds.movies.presentation.detail

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.fakes.GetMovieDetailsUseCaseFake
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
    private lateinit var useCase: GetMovieDetailsUseCaseFake
    private lateinit var viewModel: DetailViewModel

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        useCase = GetMovieDetailsUseCaseFake()
        viewModel = DetailViewModel(useCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given use case has delay, when getMovieDetail, then loading state is emitted`() = runTest {
        // arrange
        useCase.delayMillis = 1
        val states = mutableListOf<DetailUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getMovieDetail("Movie 1")
        advanceUntilIdle()

        // assert
        assertTrue(states.any { it.isLoading })
    }

    @Test
    fun `given use case returns a movie, when getMovieDetail, then final state has that movie`() = runTest {
        // arrange
        val movie = movieOf(id = 1)
        useCase.result = movie
        val states = mutableListOf<DetailUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getMovieDetail("Movie 1")

        // assert
        assertFalse(states.last().isLoading)
        assertEquals(movie, states.last().movie)
    }

    @Test
    fun `given use case returns null, when getMovieDetail, then final state has null movie`() = runTest {
        // arrange
        useCase.result = null
        val states = mutableListOf<DetailUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { states.add(it) }
        }

        // act
        viewModel.getMovieDetail("Movie 99")

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
