package edu.dyds.movies.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import kotlinx.coroutines.delay

class GetMovieDetailsUseCaseFake: GetMovieDetailsUseCase {
    var result: Movie? = null
    var shouldThrow = false
    var delayMillis: Long = 0

    override suspend fun invoke(id: Int): Movie? {
        if (delayMillis > 0) delay(delayMillis)
        if (shouldThrow) throw RuntimeException("use case error")
        return result
    }
}