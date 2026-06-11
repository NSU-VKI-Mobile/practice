package ci.nsu.moble.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.moble.main.data.dto.UserDto
import ci.nsu.moble.main.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AuthRepository) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _users = MutableStateFlow<List<UserDto>>(emptyList())
    val users = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // ДОБАВЛЕНО: состояние для ошибки
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d(TAG, "Loading users...")
            val result = repository.getUsers()
            if (result.isSuccess) {
                val userList = result.getOrNull() ?: emptyList()
                Log.d(TAG, "Loaded ${userList.size} users")
                _users.value = userList
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Неизвестная ошибка"
                Log.e(TAG, "Failed to load users: $errorMessage")
                _error.value = errorMessage
            }
            _isLoading.value = false
        }
    }
}