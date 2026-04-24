package edu.dyds.movies.domain.error

sealed class AppError {
    abstract fun toUserMessage(): String

    sealed class Network : AppError() {
        data object NoConnection : Network() {
            override fun toUserMessage() = "No internet connection"
        }
        data object Timeout : Network() {
            override fun toUserMessage() = "Connection timed out. Try again"
        }
        data class Http(val code: Int) : Network() {
            override fun toUserMessage() = "Server error ($code). Try again later"
        }
    }
    data class Unexpected(val cause: Throwable? = null) : AppError() {
        override fun toUserMessage() = "An unexpected error occurred"
    }
}