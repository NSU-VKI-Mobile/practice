package ci.nsu.mobile.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.auth.data.models.AuthResponse
import ci.nsu.mobile.auth.data.models.GroupDto
import ci.nsu.mobile.auth.data.models.RegisterRequest
import ci.nsu.mobile.auth.data.repository.ApiResult
import ci.nsu.mobile.auth.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    // Состояние экрана входа
    private val _loginState = MutableStateFlow<ApiResult<AuthResponse>?>(null)
    val loginState: StateFlow<ApiResult<AuthResponse>?> = _loginState.asStateFlow()

    // Состояние экрана регистрации
    private val _registerState = MutableStateFlow<ApiResult<Unit>?>(null)
    val registerState: StateFlow<ApiResult<Unit>?> = _registerState.asStateFlow()

    // Список групп (для экрана регистрации)
    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups.asStateFlow()
    private val _groupsLoading = MutableStateFlow(false)
    val groupsLoading: StateFlow<Boolean> = _groupsLoading.asStateFlow()

    // Общее состояние загрузки
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Общая ошибка
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.login(login, password)
            _loginState.value = result
            if (result is ApiResult.Error) {
                _error.value = result.message
            }
            _isLoading.value = false
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.register(request)
            _registerState.value = result
            if (result is ApiResult.Error) {
                _error.value = result.message
            }
            _isLoading.value = false
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            _groupsLoading.value = true
            val result = repository.getGroups()
            if (result is ApiResult.Success) {
                _groups.value = result.data
            } else if (result is ApiResult.Error) {
                _error.value = result.message
            }
            _groupsLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun clearStates() {
        _loginState.value = null
        _registerState.value = null
        _error.value = null
    }
}