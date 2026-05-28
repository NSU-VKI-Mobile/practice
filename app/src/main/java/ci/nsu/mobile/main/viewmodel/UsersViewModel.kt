package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.models.UserDto
import ci.nsu.mobile.main.data.repositories.AuthRepository
import ci.nsu.mobile.main.data.repositories.ApiResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UsersViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users: StateFlow<List<UserDto>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = authRepository.getUsers()
            when (result) {
                is ApiResult.Success -> {
                    _users.value = result.data
                }
                is ApiResult.Error -> {
                    _error.value = result.message
                }
                else -> {}
            }
            _isLoading.value = false
        }
    }
}