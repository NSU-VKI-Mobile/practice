package ci.nsu.moble.main.auth.data.repository

sealed class MyAuthApiResult<out T> {
    data class Success<T>(val data: T) : MyAuthApiResult<T>()
    data class Error(val message: String, val code: Int? = null) : MyAuthApiResult<Nothing>()
    object Loading : MyAuthApiResult<Nothing>()
}