package ci.nsu.mobile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Временный ApiResult
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

class AuthViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<ApiResult<Unit>?>(null)
    val loginState: StateFlow<ApiResult<Unit>?> = _loginState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            // Имитация запроса
            kotlinx.coroutines.delay(1000)
            _isLoading.value = false
            _loginState.value = ApiResult.Success(Unit)
        }
    }

    fun clearStates() {
        _loginState.value = null
    }
}