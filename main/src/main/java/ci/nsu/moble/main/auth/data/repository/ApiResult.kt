package ci.nsu.moble.main.auth.data.repository

sealed class AuthApiResult<out T> {
    data class Success<T>(val data: T) : AuthApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : AuthApiResult<Nothing>()
    object Loading : AuthApiResult<Nothing>()
}