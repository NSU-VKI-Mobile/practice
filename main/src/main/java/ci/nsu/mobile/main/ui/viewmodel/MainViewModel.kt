package ci.nsu.mobile.main.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.main.data.api.TokenManager
import ci.nsu.mobile.main.data.model.UserDto
import ci.nsu.mobile.main.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi

class MainViewModel : ViewModel() {
    private val repository = AuthRepository()

    @OptIn(InternalSerializationApi::class)
    var users by mutableStateOf<List<UserDto>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadUsers()
    }

    @OptIn(InternalSerializationApi::class)
    fun loadUsers() {
        viewModelScope.launch {
            isLoading = true
            repository.getUsers()
                .onSuccess { users = it }
                .onFailure { errorMessage = it.message }
            isLoading = false
        }
    }

    fun logout() {
        TokenManager.clear()
    }
}