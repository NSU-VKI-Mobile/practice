package ci.nsu.mobile.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ci.nsu.mobile.data.model.*
import ci.nsu.mobile.data.repository.AuthRepository
import ci.nsu.mobile.utils.TokenManager
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)
    var isLoggedIn by mutableStateOf(false)

    var users by mutableStateOf<List<UserDto>>(emptyList())
    var groups by mutableStateOf<List<GroupDto>>(emptyList())

    fun login(login: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            error = null

            val result = repo.login(login, password)

            isLoading = false

            result.onSuccess {
                TokenManager.token = it
                isLoggedIn = true
            }.onFailure {
                error = it.message
            }
        }
    }

    fun register(request: RegisterRequest) {
        viewModelScope.launch {
            isLoading = true
            val result = repo.register(request)
            isLoading = false

            result.onSuccess {
                TokenManager.token = it
                isLoggedIn = true
            }.onFailure {
                error = it.message
            }
        }
    }

    fun loadUsers() {
        viewModelScope.launch {

            Log.d("API", "Loading users...")

            val result = repo.getUsers()

            result.onSuccess {
                Log.d("API", "Users size = ${it.size}")
                users = it
            }.onFailure {
                Log.e("API", "ERROR: ${it.message}")
            }
        }
    }

    fun loadGroups() {
        viewModelScope.launch {
            repo.getGroups().onSuccess {
                groups = it
            }
        }
    }

    fun logout() {
        TokenManager.clear()
        isLoggedIn = false
    }
}