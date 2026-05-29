package edu.dyds.movies.domain.fakes

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.delay

class GetPopularMoviesUseCaseFake : GetPopularMoviesUseCase {
    var result: List<QualifiedMovie> = emptyList()
    var shouldThrow = false
    var delayMillis: Long = 0

    override suspend fun invoke(): List<QualifiedMovie> {
        if (delayMillis > 0) delay(delayMillis)
        if (shouldThrow) throw RuntimeException("use case error")
        return result
    }
}
