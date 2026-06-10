package ci.nsu.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.data.remote.dto.UserResponse
import ci.nsu.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ci.nsu.ui.common.NetworkResult

class UsersViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _usersState = MutableStateFlow<NetworkResult<List<UserResponse>>?>(null)
    val usersState: StateFlow<NetworkResult<List<UserResponse>>?> = _usersState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _usersState.value = NetworkResult.Loading
            try {
                val response = authRepository.getUsers()
                if (response.isSuccessful && response.body() != null) {
                    _usersState.value = NetworkResult.Success(response.body()!!)
                } else {
                    _usersState.value = NetworkResult.Error("Ошибка сервера: ${response.code()}")
                }
            } catch (e: Exception) {
                _usersState.value = NetworkResult.Error("Ошибка сети: ${e.localizedMessage}")
            }
        }
    }
}
