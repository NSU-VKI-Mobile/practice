package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.network.dto.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.util.Event
import kotlinx.coroutines.launch

class UsersViewModel(application: Application) : AndroidViewModel(application) {
    data class UiState(
        val isLoading: Boolean = false,
        val users: List<UserDto> = emptyList(),
        val error: String? = null
    )

    private val repository = AuthRepository()

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _navigateToLogin = MutableLiveData<Event<Unit>>()
    val navigateToLogin: LiveData<Event<Unit>> = _navigateToLogin

    fun loadUsers() {
        _uiState.value = (_uiState.value ?: UiState()).copy(isLoading = true, error = null)
        viewModelScope.launch {
            val result = repository.getUsers()
            result.fold(
                onSuccess = { users ->
                    _uiState.value = UiState(isLoading = false, users = users)
                },
                onFailure = { throwable ->
                    _uiState.value = UiState(
                        isLoading = false,
                        users = emptyList(),
                        error = throwable.message ?: "Ошибка загрузки пользователей"
                    )
                }
            )
        }
    }

    fun logout() {
        repository.logout()
        _navigateToLogin.value = Event(Unit)
    }
}
